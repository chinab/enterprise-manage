#ifndef IDBTABBUTTONBARBUTTON_H
#define IDBTABBUTTONBARBUTTON_H

#include <QToolButton>
#include <QMouseEvent>
#include <QPoint>
#include <QPropertyAnimation>
#include "idblayoutwindow.h"
#include "idbtab.h"

class IDBTabButtonBarButton : public QToolButton
{
    Q_OBJECT
public:
    explicit IDBTabButtonBarButton(IDBTab *tab,QWidget *parent = 0);
    ~IDBTabButtonBarButton();
    static int WIDTH_DEFULT;
    static int HEIGHT_DEFULT;
    void setParentWindow(IDBLayoutWindow *);
    void setTabIndex(const int &tabIndex);
    int tabIndex();
    void moveByIndex();
    IDBTab *tab();
    void open();

public slots:
    void animationFinishedHandler();

protected:
    void mousePressEvent(QMouseEvent *event);
    void mouseMoveEvent(QMouseEvent *event);
    void mouseReleaseEvent(QMouseEvent *event);

private:
    int mouseOffset_;
    int tabIndex_;
    QPropertyAnimation *animation_;
    IDBLayoutWindow * parentWindow_;
    IDBTab *tab_;

};

#endif // IDBTABBUTTONBARBUTTON_H
