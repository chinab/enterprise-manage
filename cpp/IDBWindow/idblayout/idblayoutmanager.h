#ifndef IDBLAYOUTMANAGER_H
#define IDBLAYOUTMANAGER_H

#include <QList>
#include <QVariant>
#include "idblayoutwindow.h"
#include "idbtab.h"

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
    IDBTab *createTab(IDBLayoutWindow *,const QVariant &);
    void fromJson(const QByteArray &);

private:
    IDBLayoutManager();
    IDBLayoutManager(const IDBLayoutManager &);
    const IDBLayoutManager& operator=(const IDBLayoutManager &);
    QList<IDBLayoutWindow *> windows;

};

#endif // IDBLAYOUTMANAGER_H
