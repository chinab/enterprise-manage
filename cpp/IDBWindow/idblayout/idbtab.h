#ifndef IDBTAB_H
#define IDBTAB_H

#include <QWidget>
#include "idbtabcontent.h"
#include "idbtabtoolbuttonbox.h"

class IDBLayoutWindow;

namespace Ui {
class IDBTab;
}

class IDBTab : public QWidget
{
    Q_OBJECT

public:
    explicit IDBTab(QWidget *parent = 0);
    ~IDBTab();
    void open();
    void info();
    void setParentWindow(IDBLayoutWindow *);
    QString name();
    void setName(const QString &);
    void setTabContent(IDBTabContent *);
    void setTabToolButtonBox(IDBTabToolButtonBox *);

private:
    Ui::IDBTab *ui;
    IDBLayoutWindow * parentWindow_;
    QString name_;
    QString type_;

};

#endif // IDBTAB_H
