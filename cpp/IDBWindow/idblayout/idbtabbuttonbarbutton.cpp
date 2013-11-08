#include "idbtabbuttonbarbutton.h"
#include <QMimeData>
#include <QDebug>
#include <QPainter>
#include "idbdrag.h"
#include "idbtabbuttonbarlist.h"
#include "idblayoutmanager.h"

int IDBTabButtonBarButton::WIDTH_DEFULT = 50;
int IDBTabButtonBarButton::HEIGHT_DEFULT = 25;

IDBTabButtonBarButton::IDBTabButtonBarButton(IDBTab *tab ,QWidget *parent) :
    QToolButton(parent)
{
    setGeometry(0,0,WIDTH_DEFULT,HEIGHT_DEFULT);
    tab_ = tab;
    setText(tab->name());
    mouseOffset_ = 0;
    tabIndex_ = 0;
    animation_ = new QPropertyAnimation(this, "pos");
    connect(animation_,SIGNAL(finished()),this,SLOT(animationFinishedHandler()));
}

IDBTabButtonBarButton::~IDBTabButtonBarButton(){
    delete animation_;
    qDebug() << "tab closed!";
}

void IDBTabButtonBarButton::setParentWindow(IDBLayoutWindow * parentWindow){
    parentWindow_ = parentWindow;
    if(parentWindow_==NULL){
        setParent(NULL);
    }else{
        setParent(parentWindow_->buttonBarList());
    }
}

void IDBTabButtonBarButton::mousePressEvent(QMouseEvent *event)
{
    mouseOffset_ = x() - event->screenPos().x();
    this->raise();
    open();
}

void IDBTabButtonBarButton::mouseMoveEvent(QMouseEvent *event)
{
    int newX = event->screenPos().x()+mouseOffset_;
//    IDBTabButtonBarList *list = qobject_cast<IDBTabButtonBarList *>(parentWidget());
    if (event->pos().y() > -20 &&
            event->pos().y() < HEIGHT_DEFULT+20 &&
            newX > -WIDTH_DEFULT &&
            newX < parentWindow_->width() - 200 ) {
        move(newX,0);
        int newTabIndex = (newX+WIDTH_DEFULT/2)/WIDTH_DEFULT;
        parentWindow_->moveButton(tabIndex_,newTabIndex);
    }else{
        parentWindow_->removeButton(tabIndex_);
        QByteArray itemData;
        QDataStream dataStream(&itemData, QIODevice::WriteOnly);
        QString tabName = text();
        dataStream << tabName;
        QMimeData *mimeData = new QMimeData();
        mimeData->setData("tab/x-idblayoutwindow-idbtab", itemData);

        QPixmap pixmap(300,200);
        pixmap.fill( Qt::black );
        QPainter painter(&pixmap);

        painter.setPen( Qt::gray );
        painter.fillRect(QRectF(20,0,200,25),Qt::gray);
        painter.drawRect(0,0,299,199);

        painter.setPen( Qt::black );
        QPoint point = QPoint( 30, 15 );
        painter.drawText( point, tabName );

        IDBDrag drag(this);
        drag.setMimeData(mimeData);
        drag.setHotSpot(QPoint(0, 0));
        drag.setPixmap(pixmap);
        QPixmap cursorPixmap(1,1);
        cursorPixmap.fill();
        drag.setDragCursor(cursorPixmap,Qt::IgnoreAction);

        switch (drag.exec(Qt::MoveAction)) {
        case Qt::MoveAction:
            mouseOffset_ = 0;
            moveByIndex();
            break;
        case Qt::IgnoreAction:
            mouseOffset_ = 0;
            IDBLayoutManager::instance().createWindow(this);
            break;
        default:
            break;
        }
        IDBLayoutManager::instance().tryCloseWindow();
    }
}

void IDBTabButtonBarButton::mouseReleaseEvent(QMouseEvent *)
{
    mouseOffset_ = 0;
    moveByIndex();
}


void IDBTabButtonBarButton::setTabIndex(const int &tabIndex){
    tabIndex_ = tabIndex;
    moveByIndex();
}

void IDBTabButtonBarButton::moveByIndex(){
    if(mouseOffset_==0){
        animation_->setDuration(100);
        animation_->setEndValue(QPointF(IDBTabButtonBarButton::WIDTH_DEFULT*tabIndex_, 0));
        animation_->start();
    }
}

void IDBTabButtonBarButton::animationFinishedHandler(){
    move(IDBTabButtonBarButton::WIDTH_DEFULT*tabIndex_, 0);
}

int IDBTabButtonBarButton::tabIndex(){
    return tabIndex_;
}

IDBTab *IDBTabButtonBarButton::tab(){
    return tab_;
}

void IDBTabButtonBarButton::open(){
    parentWindow_->openTab(tabIndex_);
}
