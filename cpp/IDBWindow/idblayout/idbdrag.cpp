#include "idbdrag.h"
#include <QDebug>
#include <QCursor>
#include <QApplication>

IDBDrag::IDBDrag(QObject * parent):QDrag(parent)
{
    qpix = QPixmap(":/resource/bcm_warning_22.png");
}

