#ifndef IDBDRAG_H
#define IDBDRAG_H
#include <QDrag>
#include <QPixmap>

class IDBDrag : public QDrag
{

    Q_OBJECT

public:
    IDBDrag(QObject *parent=0);

private:
    QPixmap qpix;

public slots:
};

#endif // IDBDRAG_H
