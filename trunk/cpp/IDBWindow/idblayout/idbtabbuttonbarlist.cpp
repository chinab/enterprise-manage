#include "idbtabbuttonbarlist.h"

#include <QDrag>
#include <QDragEnterEvent>
#include <QMimeData>
#include <QDebug>
#include <QApplication>
#include <QPainter>
#include "idblayoutmanager.h"
#include "idbdrag.h"

IDBTabButtonBarList::IDBTabButtonBarList(QWidget *parent)
    : QWidget(parent){
    setAcceptDrops(true);
    dragButton = NULL;
}

void IDBTabButtonBarList::setParentWindow(IDBLayoutWindow * parentWindow){
    parentWindow_ = parentWindow;
}

void IDBTabButtonBarList::dragEnterEvent(QDragEnterEvent *event){
    if (event->mimeData()->hasFormat("tab/x-idblayoutwindow-idbtab")){
        dragButton = qobject_cast<IDBTabButtonBarButton *>(event->source());
        int newTabIndex = (event->pos().x()+IDBTabButtonBarButton::WIDTH_DEFULT/2)/IDBTabButtonBarButton::WIDTH_DEFULT;
        addButton(dragButton,newTabIndex);
        event->accept();
        activateWindow();
    } else{
        event->ignore();
    }
}

void IDBTabButtonBarList::dragLeaveEvent(QDragLeaveEvent *){
    if(dragButton!=NULL){
        parentWindow_->removeButton(dragButton->tabIndex());
        dragButton=NULL;
        parentWindow_->moveByIndexAll();
    }
}

void IDBTabButtonBarList::dragMoveEvent(QDragMoveEvent *event){
    if (event->mimeData()->hasFormat("tab/x-idblayoutwindow-idbtab")){
        dragButton = qobject_cast<IDBTabButtonBarButton *>(event->source());
        int newX = event->pos().x()-30;
        dragButton->move(newX,0);
        int newTabIndex = (newX+IDBTabButtonBarButton::WIDTH_DEFULT/2)/IDBTabButtonBarButton::WIDTH_DEFULT;
        parentWindow_->moveButton(dragButton->tabIndex(),newTabIndex);
    } else{
        event->ignore();
    }
}

void IDBTabButtonBarList::dropEvent(QDropEvent *event){
    if (event->source() != this) {
        if (event->mimeData()->hasFormat("tab/x-idblayoutwindow-idbtab")) {
            event->setDropAction(Qt::MoveAction);
            event->accept();
        } else {
            event->ignore();
        }
    }else{
        QWidget::dropEvent(event);
    }
}

void IDBTabButtonBarList::addButton(IDBTabButtonBarButton *button,const int &index){
    int tabSize = parentWindow_->tabSize();

    parentWindow_->addTabToTabs(button->tab());
    parentWindow_->addButtonToButtons(button);
    button->show();
    button->setTabIndex(tabSize);
    if(index<tabSize){
        parentWindow_->moveButton(tabSize,index);
    }
    parentWindow_->openTab(button->tabIndex());
}
