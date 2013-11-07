#include "idbdeal.h"
#include "ui_idbdeal.h"

IDBDeal::IDBDeal(QWidget *parent) :
    IDBTabContent(parent),
    ui(new Ui::IDBDeal)
{
    ui->setupUi(this);
}

IDBDeal::~IDBDeal()
{
    delete ui;
}
