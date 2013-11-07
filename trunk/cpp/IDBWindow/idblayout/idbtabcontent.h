#ifndef IDBTABCONTENT_H
#define IDBTABCONTENT_H

#include <QWidget>
#include <QVariantMap>

class IDBTabContent : public QWidget
{
    Q_OBJECT
public:
    explicit IDBTabContent(QWidget *parent = 0);
    virtual void setInfo(const QVariantMap &);
    virtual void info(QVariantMap &);

signals:

public slots:

};

#endif // IDBTABCONTENT_H
