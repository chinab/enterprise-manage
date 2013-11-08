#include "idblayoutmanager.h"
#include <QJsonDocument>
#include <QDebug>

#include "idbtemplate.h"
#include "idbdeal.h"

IDBLayoutManager::IDBLayoutManager(){
    willRemoveWindow = NULL;
}

IDBLayoutManager &IDBLayoutManager::instance(){
    static IDBLayoutManager instance;
    return instance;
}

void IDBLayoutManager::fromJson(const QByteArray &json){
    QJsonDocument jsonDoc= QJsonDocument::fromJson(json);
    QVariant result = jsonDoc.toVariant();
    QVariantList windowVars = result.toMap()["windows"].toList();
    foreach (QVariant (windowInfo), windowVars) {
       IDBLayoutWindow * window = createWindow(windowInfo);
       window->show();
       windows_.append(window);
    }
}

void IDBLayoutManager::createWindow(IDBTabButtonBarButton *button){
    IDBLayoutWindow *window = new IDBLayoutWindow();
    window->setGeometry(QCursor::pos().x(),QCursor::pos().y(),
                        button->tab()->width(),button->tab()->height());
    window->show();
    windows_.append(window);
    window->addButton(button);
}

IDBLayoutWindow *IDBLayoutManager::createWindow(const QVariant &info){
    QVariantMap windowMap = info.toMap();

    IDBLayoutWindow *window = new IDBLayoutWindow();
    window->setGeometry(windowMap["x"].toInt(),windowMap["y"].toInt(),windowMap["width"].toInt(),windowMap["height"].toInt());
    QVariantList tabVars =windowMap["tabs"].toList();
    foreach (QVariant (tabInfo), tabVars) {
        createTab(window,tabInfo);
    }
    window->openTab(windowMap["currTabIndex"].toInt());
    return window;
}

IDBTab *IDBLayoutManager::createTab(IDBLayoutWindow * parentWindow,const QVariant &info){
    QVariantMap tabMap = info.toMap();
    IDBTab *tab = new IDBTab();
    tab->setName(tabMap["name"].toString());
    parentWindow->addTab(tab);

    int type = tabMap["type"].toInt();
    IDBTabContent * tabContent;
    switch (type) {
    case IDBLayout::Template:
        tabContent = new IDBTemplate();
        break;
    case IDBLayout::Deal:
        tabContent = new IDBDeal();
        break;
    default:
        break;
    }

    tabContent->setInfo(tabMap["info"].toMap());

    tab->setTabContent(tabContent);
    return tab;
}

void IDBLayoutManager::tryPrepareCloseWindow(IDBLayoutWindow *window){
    if(window!=NULL&&window->tabSize()==0){
        if(windows_.contains(window)){
            windows_.removeOne(window);
        }
        /**
        window->close();
        delete window;
        @ses doTypeCloseWindow
        **/
        window->setVisible(false);
        willRemoveWindow = window;
        qDebug() << "window Prepare close!";
    }
}

void IDBLayoutManager::tryCloseWindow(){
    if(willRemoveWindow!=NULL){
        willRemoveWindow->close();
        delete willRemoveWindow;
        willRemoveWindow = NULL;
        qDebug() << "window closed!";
    }
}
