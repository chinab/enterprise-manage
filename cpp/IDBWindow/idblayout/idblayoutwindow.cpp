#include "idblayoutwindow.h"
#include "idbtab.h"
#include "idbtabbuttonbarbutton.h"
#include "idbtabbuttonbarlist.h"
#include <QDebug>
#include <QBoxLayout>
#include <QMimeData>
#include "idblayoutmanager.h"

IDBLayoutWindow::IDBLayoutWindow(QWidget *parent) :
    QMainWindow(parent)
{
    tabParent_ = new QWidget(this);
    buttonBarList_ = new IDBTabButtonBarList(this);
    buttonBarList_->setParentWindow(this);
    currTab_ = NULL;

    setMinimumWidth(800);
    setMinimumHeight(500);

    setAcceptDrops(true);
}

void IDBLayoutWindow::resizeEvent( QResizeEvent *event )
{
    tabParent_->setGeometry(0,0,this->width(),this->height());
    buttonBarList_->setGeometry(0,40,this->width()-200,30);
    if(currTab_!=NULL){
        currTab_->setGeometry(0,0,this->width(),this->height());
    }
    return QMainWindow::resizeEvent(event);
}

IDBLayoutWindow::~IDBLayoutWindow(){
    delete tabParent_;
    delete buttonBarList_;
    if(buttons_.size()>0){
        foreach (IDBTabButtonBarButton *button, buttons_) {
            delete button;
        }
    }
    if(tabs_.size()>0){
        foreach (IDBTab *tab, tabs_) {
            delete tab;
        }
    }
}

bool IDBLayoutWindow::containsTab(IDBTab *tab){
    return tabs_.contains(tab);
}

bool IDBLayoutWindow::containsButton(IDBTabButtonBarButton *button){
    return buttons_.contains(button);
}

void IDBLayoutWindow::addTab(IDBTab *tab){
    if(!containsTab(tab)){
        IDBTabButtonBarButton *button = new IDBTabButtonBarButton(tab,this);
        tab->setParentWindow(this);
        button->setParentWindow(this);
        button->setTabIndex(tabSize());
        addTabToTabs(tab);
        addButtonToButtons(button);
    }
}

void IDBLayoutWindow::addButton(IDBTabButtonBarButton *button){
    buttonBarList_->addButton(button,buttons_.size());
    addTabToTabs(button->tab());
    addButtonToButtons(button);
}

void IDBLayoutWindow::openTab(IDBTab *tab){
    foreach (IDBTab *tabTemp, tabs_) {
        if(tabTemp!=tab){
            tabTemp->setGeometry(-width()*2,-height()*2,width(),height());
        }
    }
    tab->setGeometry(0,0,width(),height());
    currTab_ = tab;
    currTab_->show();
    currTab_->raise();
}

void IDBLayoutWindow::openTab(const int &index){
    if(buttons_.size()<=0){
        return;
    }
    int openIndex = 0;
    if(buttons_.size()>index&&index>=0){
        openIndex = index;
    }
    foreach (IDBTabButtonBarButton *button, buttons_) {
        if(button->tabIndex()==openIndex){
            openTab(button->tab());
            button->setProperty("isOpen",true);
        }else{
            button->setProperty("isOpen",false);
        }
        button->style()->polish(button);
    }
}

//QList<IDBTab *>& IDBLayoutWindow::tabs(){
//    return tabs_;
//}

//QList<IDBTabButtonBarButton *>& IDBLayoutWindow::buttons_{
//    return buttons_;
//}

QWidget *IDBLayoutWindow::tabParent(){
    return tabParent_;
}
IDBTabButtonBarList *IDBLayoutWindow::buttonBarList(){
    return buttonBarList_;
}

void IDBLayoutWindow::addTabToTabs(IDBTab *tab){
    if(!containsTab(tab)){
        tabs_.append(tab);
    }
    tab->setParentWindow(this);
}

void IDBLayoutWindow::removeTabFromTabs(IDBTab *tab){
    if(containsTab(tab)){
        tabs_.removeOne(tab);
    }
    tab->setParentWindow(NULL);
}

void IDBLayoutWindow::addButtonToButtons(IDBTabButtonBarButton *button){
    if(!containsButton(button)){
        buttons_.append(button);
    }
    button->setParentWindow(this);
}

void IDBLayoutWindow::removeButtonFromButtons(IDBTabButtonBarButton *button){
    if(containsButton(button)){
        buttons_.removeOne(button);
    }
    button->setParentWindow(NULL);
}

void IDBLayoutWindow::dragEnterEvent(QDragEnterEvent *event){
    if (event->mimeData()->hasFormat("tab/x-idblayoutwindow-idbtab")){
        this->activateWindow();
    }
    QMainWindow::dragEnterEvent(event);
}

int IDBLayoutWindow::tabSize(){
    return buttons_.size();
}

IDBTabButtonBarButton *IDBLayoutWindow::getButtonByIndex(const int &tabIndex){
    IDBTabButtonBarButton *currButton = NULL;
    foreach (IDBTabButtonBarButton *button, buttons_) {
        if(button->tabIndex()==tabIndex){
            currButton = button;
        }
    }
    return currButton;
}

void IDBLayoutWindow::moveButton(const int &fromTabIndex,const int &toTabIndex){
    if(toTabIndex>=tabSize()||
        toTabIndex<0||
        fromTabIndex>=tabSize()||
        fromTabIndex<0){
        return;
    }
    IDBTabButtonBarButton *currButton = getButtonByIndex(fromTabIndex);
    if(fromTabIndex>toTabIndex){
        foreach (IDBTabButtonBarButton *button, buttons_) {
            if(button->tabIndex()>=toTabIndex && button->tabIndex()<fromTabIndex ){
                button->setTabIndex(button->tabIndex()+1);
            }
        }
    }else if(fromTabIndex<toTabIndex){
        foreach (IDBTabButtonBarButton *button, buttons_) {
            if(button->tabIndex()<=toTabIndex && button->tabIndex()>fromTabIndex ){
                button->setTabIndex(button->tabIndex()-1);
            }
        }
    }
    if(currButton!=NULL){
        currButton->setTabIndex(toTabIndex);
    }
}

void IDBLayoutWindow::removeButton(const int &tabIndex){
    if(tabIndex>=buttons_.size()||
        tabIndex<0){
        return;
    }
    IDBTabButtonBarButton *currButton = getButtonByIndex(tabIndex);
    foreach (IDBTabButtonBarButton *button, buttons_) {
        if(button->tabIndex()>tabIndex ){
            button->setTabIndex(button->tabIndex()-1);
        }
    }
    if(currButton!=NULL){
        removeTabFromTabs(currButton->tab());
        removeButtonFromButtons(currButton);
        currButton->setParent(NULL);
        if(tabIndex>0){
            openTab(tabIndex-1);
        }else{
            openTab(0);
        }
    }
    IDBLayoutManager::instance().tryPrepareCloseWindow(this);
}

void IDBLayoutWindow::moveByIndexAll(){
    foreach (IDBTabButtonBarButton *button, buttons_) {
        button->moveByIndex();
    }
}
