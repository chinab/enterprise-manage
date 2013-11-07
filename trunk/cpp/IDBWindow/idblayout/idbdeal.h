#ifndef IDBDEAL_H
#define IDBDEAL_H

#include <QWidget>
#include "idbtabcontent.h"

namespace Ui {
class IDBDeal;
}

class IDBDeal : public IDBTabContent
{
    Q_OBJECT

public:
    explicit IDBDeal(QWidget *parent = 0);
    ~IDBDeal();

private:
    Ui::IDBDeal *ui;
};

#endif // IDBDEAL_H
