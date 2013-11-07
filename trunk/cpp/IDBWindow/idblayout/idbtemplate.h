#ifndef IDBTEMPLATE_H
#define IDBTEMPLATE_H

#include <QWidget>
#include "idbtabcontent.h"

namespace Ui {
class IDBTemplate;
}

class IDBTemplate : public IDBTabContent
{
    Q_OBJECT

public:
    explicit IDBTemplate(QWidget *parent = 0);
    ~IDBTemplate();
    void setInfo(const QVariantMap &);
    void info(QVariantMap &);

private:
    Ui::IDBTemplate *ui;
};

#endif // IDBTEMPLATE_H
