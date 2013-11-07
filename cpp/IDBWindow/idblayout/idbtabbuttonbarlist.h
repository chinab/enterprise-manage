#ifndef IDBTabButtonBarList_H
#define IDBTabButtonBarList_H

#include <QWidget>
#include <QDragEnterEvent>
#include <QDropEvent>
#include <QDragLeaveEvent>
#include <QDragMoveEvent>
#include <QListWidget>
#include "idblayoutwindow.h"
#include "idbtabbuttonbarbutton.h"
#include "idbtab.h"

class IDBTabButtonBarList : public QWidget
{
    Q_OBJECT

public:
    explicit IDBTabButtonBarList(QWidget *parent = 0);
    void setParentWindow(IDBLayoutWindow *);
    void addTab(IDBTab *tab,const int &index);
    void addButton(IDBTabButtonBarButton *button,const int &index);
    void moveButton(const int &fromTabIndex,const int &toTabIndex);
    void removeButton(const int &tabIndex);
    void moveByIndexAll();

protected:
    void dragEnterEvent(QDragEnterEvent *);
    void dragMoveEvent(QDragMoveEvent *);
    void dragLeaveEvent(QDragLeaveEvent *);
    void dropEvent(QDropEvent *);

public slots:
//    void actionChangedHandler(Qt::DropAction);
//    void targetChangedHandler(QObject *newTarget);

private:
    IDBLayoutWindow * parentWindow_;
    IDBTabButtonBarButton *getButtonByIndex(const int &tabIndex);
    IDBTabButtonBarButton *dragButton;
};

#endif // IDBTabButtonBarList_H
