#include "idbtemplate.h"
#include "ui_idbtemplate.h"

IDBTemplate::IDBTemplate(QWidget *parent) :
    IDBTabContent(parent),
    ui(new Ui::IDBTemplate)
{
    ui->setupUi(this);
}

IDBTemplate::~IDBTemplate()
{
    delete ui;
}


void IDBTemplate::setInfo(const QVariantMap &info){
    ui->typeLabel->setText(info["type"].toString());
    ui->nameLabel->setText(info["name"].toString());
}

void IDBTemplate::info(QVariantMap &info){
    info["name"] = "haha";
}
