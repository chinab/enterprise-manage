#ifndef IDBLAYOUTMANAGER_H
#define IDBLAYOUTMANAGER_H

#include <QList>
#include <QVariant>
#include "idblayoutwindow.h"
#include "idbtab.h"
#include "idbtabbuttonbarbutton.h"

namespace IDBLayout {
    enum IDBTabType{
        Template = 1,
        Deal
    };
}

class IDBLayoutManager
{
public:
    static IDBLayoutManager &instance();

    IDBLayoutWindow *createWindow(const QVariant &);
    void createWindow(IDBTabButtonBarButton *);
    IDBTab *createTab(IDBLayoutWindow *,const QVariant &);
    void fromJson(const QByteArray &);
    void tryPrepareCloseWindow(IDBLayoutWindow *);
    void tryCloseWindow();

private:
    IDBLayoutManager();
    IDBLayoutManager(const IDBLayoutManager &);
    const IDBLayoutManager& operator=(const IDBLayoutManager &);
    QList<IDBLayoutWindow *> windows_;
    IDBLayoutWindow *willRemoveWindow;

};

#endif // IDBLAYOUTMANAGER_H
