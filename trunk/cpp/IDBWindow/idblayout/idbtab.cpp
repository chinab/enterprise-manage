#include "idbtab.h"
#include "ui_idbtab.h"
#include "idblayoutwindow.h"
#include <QHBoxLayout>
#include <QDebug>

IDBTab::IDBTab(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::IDBTab)
{
    ui->setupUi(this);
    parentWindow_ = NULL;
}

IDBTab::~IDBTab()
{
    delete ui;
    qDebug() << "tab closed!";
}

void IDBTab::setParentWindow(IDBLayoutWindow * parentWindow){
    parentWindow_ = parentWindow;

    if(parentWindow==NULL){
        setParent(NULL);
    }else{
        setParent(parentWindow->tabParent());
    }
}

QString IDBTab::name(){
    return name_;
}

void IDBTab::setName(const QString &name){
    name_ = name;
}


void IDBTab::setTabContent(IDBTabContent * tabContent){
    QHBoxLayout *layout = new QHBoxLayout(ui->tabContent);
    layout->addWidget(tabContent);
    tabContent->setParent(ui->tabContent);
    ui->tabContent->setLayout(layout);
}

void IDBTab::setTabToolButtonBox(IDBTabToolButtonBox * tabToolButtonBox){
    QHBoxLayout *layout = new QHBoxLayout(ui->tabToolButtonBox);
    layout->addWidget(tabToolButtonBox);
    tabToolButtonBox->setParent(ui->tabToolButtonBox);
    ui->tabToolButtonBox->setLayout(layout);
}

