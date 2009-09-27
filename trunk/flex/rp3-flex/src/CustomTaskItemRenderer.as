///////////////////////////////////////////////////////////////////////////////
//
//  Copyright (C) 2007-2008 ILOG, S.A.
//  All Rights Reserved.
//  The following is ILOG Sample Code.  Any usage of the ILOG Sample Code is  
//  subject to the terms and conditions of the ILOG End User License   
//  Agreement applicable to this ILOG software product. 
//
///////////////////////////////////////////////////////////////////////////////

package
{
  import flash.display.GradientType;
  import flash.filters.GlowFilter;
  import flash.geom.Rectangle;
  
  import ilog.gantt.GanttSheet;
  import ilog.gantt.TaskItem;
  import ilog.utils.TimeUnit;
  
  import mx.controls.Image;
  import mx.controls.listClasses.IListItemRenderer;
  import mx.core.IDataRenderer;
  import mx.core.UIComponent;
  import mx.core.UITextField;
  import mx.events.FlexEvent;
  import mx.graphics.GradientEntry;
  import mx.graphics.LinearGradient;
  import mx.utils.ColorUtil;

  [Event(name="dataChange", type="mx.events.FlexEvent")]

  public class CustomTaskItemRenderer extends UIComponent
    implements IDataRenderer, IListItemRenderer
  {
    //--------------------------------------------------------------------------
    //
    //  Class constants
    //
    //--------------------------------------------------------------------------
    [Embed(source="../resources/sick.png")] 
    private static const sickIconClass:Class; 
          
    [Embed(source="../resources/travel.png")] 
    private static const travelIconClass:Class; 
          
    [Embed(source="../resources/notpresent.png")] 
    private static const missionIconClass:Class; 
        
    [Embed(source="../resources/vacation.png")] 
    private static const vacationIconClass:Class; 
    
    [Embed(source="../resources/user1.png")] 
    private static const stopIconClass:Class; 

    private static const reasonToColor:Object = {
      R1: 0x56CD4D,
      R2: 0xFF002F,
      R3: 0x2D00FF,
      R4: 0xFF9900,
      R4: 0x00ABFF
    };
        
    private static const reasonToIcon:Object = {
      R1: travelIconClass,
      R2: missionIconClass,
      R3: sickIconClass,
      R4: vacationIconClass,
      R5:stopIconClass
    };

    //--------------------------------------------------------------------------
    //
    //  Variables
    //
    //--------------------------------------------------------------------------
    
    private var reason:String; 
    private var icon:Image;
    private var label:UITextField;
    
    //--------------------------------------------------------------------------
    //
    //  Constructor
    //
    //--------------------------------------------------------------------------
    public function CustomTaskItemRenderer()
    {
      this.mouseChildren = false;
    }
    
    //--------------------------------------------------------------------------
    //
    //  Properties
    //
    //--------------------------------------------------------------------------
    
    //---------------------------------
    // data
    //---------------------------------
    [Bindable("dataChange")]
    /**
     * @private 
     * Internal storage for the property value.
     */
    private var _data:Object;
    private var _dataChanged:Boolean;
    
    public function get data():Object
    {
      return _data;
    }
    
    public function set data(value:Object):void
    {
      _dataChanged = true;
      _data = value;

      invalidateProperties();
      
      dispatchEvent(new FlexEvent(FlexEvent.DATA_CHANGE));
    }

    //---------------------------------
    //  taskItem
    //---------------------------------
    private function get taskItem():TaskItem
    {
      return _data as TaskItem;  
    }
    
    //--------------------------------------------------------------------------
    //
    //  Overriden methods from UIComponent
    //
    //--------------------------------------------------------------------------
    /**
     * @private
     */
    override protected function createChildren():void
    {
      super.createChildren();
      if (icon == null)
      {
        icon = new Image();
        var ef:GlowFilter = new GlowFilter(0xEEEEEE, 0.5, 2, 2, 5, 5,false,false);
        icon.filters = [ef];
        //addChild(icon);
      }
      if (label == null)
      {
        label = new UITextField();
        label.styleName = this;
        label.ignorePadding = false;
        label.includeInLayout = false;
        //addChild(label);
      }      
    }

    /**
     * @private
     */
    override protected function commitProperties():void
    {
      super.commitProperties();
      
      if (_dataChanged)
      {
        _dataChanged = false;
        if (taskItem)
        {
          reason = String(humanresources.getFieldValue(taskItem.data, "reason"));
          label.text = String(humanresources.reasonToLabel[reason]);
        }
        else
          label.text = "";
        invalidateDisplayList();
      }
    }

    /**
     * @private
     */
    override protected function updateDisplayList(unscaledWidth:Number,
                                                  unscaledHeight:Number):void
    {
      super.updateDisplayList(unscaledWidth, unscaledHeight);

      var ganttSheet:GanttSheet = taskItem.ganttSheet;
      
      if (!ganttSheet)
      {
        draw(unscaledWidth, unscaledHeight, false, false, true);
      }
      else
      {
        var selected:Boolean = ganttSheet.isItemSelected(taskItem.data);
        var highlighted:Boolean = ganttSheet.isItemHighlighted(taskItem.data);
        var showIcon:Boolean = TimeUnit.DAY.milliseconds / ganttSheet.zoomFactor > 28;
        draw(unscaledWidth, unscaledHeight, selected, highlighted, showIcon);
      }
    }

    private function draw(unscaledWidth:Number, unscaledHeight:Number, 
                          selected:Boolean, highlighted:Boolean, 
                          showIcon:Boolean):void
    {
      var borderColor:uint;
      var backgroundColor:uint;
      var borderColors:Array;
      var stateColor:uint;
      
      graphics.clear();
    
      backgroundColor = reasonToColor[reason];
      
      if (showIcon)
      {
        icon.source = reasonToIcon[reason];
        icon.move(4,4);
        icon.setActualSize(22, 22);
        icon.visible = true;
      }
      else
      {
        icon.visible = false;
      }

      var textStartPosition:Number = (showIcon) ? (icon.x + icon.width + 1) : 0;

      if (textStartPosition + 10 <= unscaledWidth)
      {
        label.move(textStartPosition, 0);
        label.setActualSize(unscaledWidth - textStartPosition, unscaledHeight);
        label.visible = true;
      }
      else 
      {
        label.visible = false;
      }
        
      if (highlighted)
      {
        stateColor = getStyle("rollOverColor");
        filters = [new GlowFilter(stateColor,1,3,3,5,10)];
        borderColors = [stateColor, ColorUtil.adjustBrightness2(stateColor, -30)];
      }
      if (selected)
      {
        stateColor = getStyle("selectionColor");
        filters = [new GlowFilter(stateColor, 1,2,2,5,10)];
        borderColors = [stateColor, ColorUtil.adjustBrightness2(stateColor, -30)];
      }
      if (!selected && !highlighted)
      {
        stateColor = 0x888888;
        filters = [];
        borderColors = [stateColor, ColorUtil.adjustBrightness2(stateColor, -20)];
      }
                  
      // border/edge
      /**
      drawRoundRect(0, 0, unscaledWidth, unscaledHeight, null,
                    borderColors, 1,
                    verticalGradientMatrix(0, 0, unscaledWidth, unscaledHeight),
                    GradientType.LINEAR, null, null); 
*/
      // linear gradient fills
      var h1:Number = (unscaledHeight-2)/2;
      var h2:Number = unscaledHeight-2 - h1;
      
      var lg:LinearGradient = new LinearGradient();
      lg.angle = 90;
      lg.entries = [
        new GradientEntry(ColorUtil.adjustBrightness2(backgroundColor,50), 0, 1),
        new GradientEntry(backgroundColor)
      ];
      lg.begin(graphics, new Rectangle(0, 0, unscaledWidth, unscaledHeight/1.5-1));
      graphics.drawRect(1, 1, unscaledWidth, h1);
      lg.end(graphics);

      var lg2:LinearGradient = new LinearGradient();
      lg2.angle = 90;
      lg2.entries = [ 
        new GradientEntry(ColorUtil.adjustBrightness2(backgroundColor,-20), 0, 1),
        new GradientEntry(ColorUtil.adjustBrightness2(backgroundColor,0), 0.5, 1),        
        new GradientEntry(ColorUtil.adjustBrightness2(backgroundColor,50), 1, 1)
      ];        
      lg2.begin(graphics, new Rectangle(0, 0, unscaledWidth, unscaledHeight-3));
      graphics.drawRect(1, 1+h1, unscaledWidth, h2);   
      lg2.end(graphics);
    }          
  }
}
