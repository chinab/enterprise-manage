#include "idbtabbuttonbarlist.h"

#include <QDrag>
#include <QDragEnterEvent>
#include <QMimeData>
#include <QDebug>
#include <QApplication>
#include <QPainter>
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
        removeButton(dragButton->tabIndex());
        dragButton=NULL;
        moveByIndexAll();
    }
}

void IDBTabButtonBarList::dragMoveEvent(QDragMoveEvent *event){
    if (event->mimeData()->hasFormat("tab/x-idblayoutwindow-idbtab")){
        dragButton = qobject_cast<IDBTabButtonBarButton *>(event->source());
        int newX = event->pos().x()-30;
        dragButton->move(newX,0);
        int newTabIndex = (newX+IDBTabButtonBarButton::WIDTH_DEFULT/2)/IDBTabButtonBarButton::WIDTH_DEFULT;
        moveButton(dragButton->tabIndex(),newTabIndex);
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

void IDBTabButtonBarList::addTab(IDBTab *tab,const int &index){
    IDBTabButtonBarButton *button = new IDBTabButtonBarButton(tab,this);
    button->setParentWindow(parentWindow_);
    int tabSize = parentWindow_->buttons().size();
    button->setTabIndex(tabSize);
    if(index<tabSize){
        moveButton(tabSize,index);
    }
    parentWindow_->addButtonToButtons(button);
}

void IDBTabButtonBarList::addButton(IDBTabButtonBarButton *button,const int &index){
    button->setParentWindow(parentWindow_);
    button->tab()->setParentWindow(parentWindow_);
    button->setParent(this);
    button->show();
    int tabSize = parentWindow_->buttons().size();
    button->setTabIndex(tabSize);
    if(index<tabSize){
        moveButton(tabSize,index);
    }
    parentWindow_->addButtonToButtons(button);
    parentWindow_->openTab(button->tabIndex());
}

IDBTabButtonBarButton *IDBTabButtonBarList::getButtonByIndex(const int &tabIndex){
    IDBTabButtonBarButton *currButton = NULL;
    foreach (IDBTabButtonBarButton *button, parentWindow_->buttons()) {
        if(button->tabIndex()==tabIndex){
            currButton = button;
        }
    }
    return currButton;
}

void IDBTabButtonBarList::moveButton(const int &fromTabIndex,const int &toTabIndex){
//    qDebug() << parentWindow_->buttons().size();
    if(toTabIndex>=parentWindow_->buttons().size()||
        toTabIndex<0||
        fromTabIndex>=parentWindow_->buttons().size()||
        fromTabIndex<0){
        return;
    }
    IDBTabButtonBarButton *currButton = getButtonByIndex(fromTabIndex);
    if(fromTabIndex>toTabIndex){
        foreach (IDBTabButtonBarButton *button, parentWindow_->buttons()) {
            if(button->tabIndex()>=toTabIndex && button->tabIndex()<fromTabIndex ){
                button->setTabIndex(button->tabIndex()+1);
            }
        }
    }else if(fromTabIndex<toTabIndex){
        foreach (IDBTabButtonBarButton *button, parentWindow_->buttons()) {
            if(button->tabIndex()<=toTabIndex && button->tabIndex()>fromTabIndex ){
                button->setTabIndex(button->tabIndex()-1);
            }
        }
    }
    if(currButton!=NULL){
        currButton->setTabIndex(toTabIndex);
    }
}

void IDBTabButtonBarList::removeButton(const int &tabIndex){
    if(tabIndex>=parentWindow_->buttons().size()||
        tabIndex<0){
        return;
    }
    IDBTabButtonBarButton *currButton = getButtonByIndex(tabIndex);
    foreach (IDBTabButtonBarButton *button, parentWindow_->buttons()) {
        if(button->tabIndex()>tabIndex ){
            button->setTabIndex(button->tabIndex()-1);
        }
    }
    if(currButton!=NULL){
        parentWindow_->buttons().removeOne(currButton);
        currButton->setParent(NULL);
        if(tabIndex>0){
            parentWindow_->openTab(tabIndex-1);
        }else{
            parentWindow_->openTab(0);
        }
    }
}
void IDBTabButtonBarList::moveByIndexAll(){
    foreach (IDBTabButtonBarButton *button, parentWindow_->buttons()) {
        button->moveByIndex();
    }
}
