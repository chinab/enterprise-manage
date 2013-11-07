#include "idbtabcontent.h"

IDBTabContent::IDBTabContent(QWidget *parent) :
    QWidget(parent)
{
}

void IDBTabContent::setInfo(const QVariantMap &info){
    info["name"];
}

void IDBTabContent::info(QVariantMap &info){
    info["name"];
}
