#include "idblayoutwindow.h"
#include "idbtab.h"
#include "idbtabbuttonbarbutton.h"
#include "idbtabbuttonbarlist.h"
#include <QDebug>
#include <QBoxLayout>
#include <QMimeData>

IDBLayoutWindow::IDBLayoutWindow(QWidget *parent) :
    QMainWindow(parent)
{
    tabParent_ = new QWidget(this);
    buttonBarList_ = new IDBTabButtonBarList(this);
    buttonBarList_->setParentWindow(this);
    setMinimumWidth(800);
    setMinimumHeight(500);

    setAcceptDrops(true);
}

void IDBLayoutWindow::resizeEvent( QResizeEvent *event )
{
    tabParent_->setGeometry(0,0,this->width(),this->height());
    buttonBarList_->setGeometry(0,40,this->width()-200,30);
    currTab_->setGeometry(0,0,this->width(),this->height());
    return QMainWindow::resizeEvent(event);
}

IDBLayoutWindow::~IDBLayoutWindow()
{
    delete tabParent_;
    delete buttonBarList_;
    delete currTab_;
}

bool IDBLayoutWindow::containsTab(IDBTab *tab){
    return tabs_.contains(tab);
}

bool IDBLayoutWindow::containsButton(IDBTabButtonBarButton *button){
    return buttons_.contains(button);
}

void IDBLayoutWindow::addTab(IDBTab *tab){
    if(!containsTab(tab)){
        tab->setParentWindow(this);
        buttonBarList_->addTab(tab,buttons_.size());
    }
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

QList<IDBTab *>& IDBLayoutWindow::tabs(){
    return tabs_;
}

QList<IDBTabButtonBarButton *>& IDBLayoutWindow::buttons(){
    return buttons_;
}

QWidget * IDBLayoutWindow::tabParent(){
    return tabParent_;
}

void IDBLayoutWindow::addTabToTabs(IDBTab *tab){
    if(!containsTab(tab)){
        tabs_.append(tab);
    }
}

void IDBLayoutWindow::removeTabFromTabs(IDBTab *tab){
    if(containsTab(tab)){
        tabs_.removeOne(tab);
    }
}

void IDBLayoutWindow::addButtonToButtons(IDBTabButtonBarButton *button){
    if(!containsButton(button)){
        buttons_.append(button);
    }
}

void IDBLayoutWindow::removeButtonFromButtons(IDBTabButtonBarButton *button){
    if(containsButton(button)){
        buttons_.removeOne(button);
    }
}

void IDBLayoutWindow::dragEnterEvent(QDragEnterEvent *event){
    if (event->mimeData()->hasFormat("tab/x-idblayoutwindow-idbtab")){
        this->activateWindow();
    }
    QMainWindow::dragEnterEvent(event);
}
