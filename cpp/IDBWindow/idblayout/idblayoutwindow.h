#ifndef IDBWINDOW_H
#define IDBWINDOW_H

#include <QList>
#include <QMainWindow>

class IDBTab;
class IDBTabButtonBarButton;
class IDBTabButtonBarList;

class IDBLayoutWindow : public QMainWindow
{
    Q_OBJECT
public:
    explicit IDBLayoutWindow(QWidget *parent = 0);
    ~IDBLayoutWindow();
    void dragEnterEvent(QDragEnterEvent *event);

    void addTabToTabs(IDBTab *);
    void removeTabFromTabs(IDBTab *);

    void addButtonToButtons(IDBTabButtonBarButton *);
    void removeButtonFromButtons(IDBTabButtonBarButton *);

    void addTab(IDBTab *);
    void openTab(IDBTab *);
    void openTab(const int &index);
    void info();
    bool containsTab(IDBTab *);
    bool containsButton(IDBTabButtonBarButton *button);
    QWidget *tabParent();
    QList<IDBTab *>& tabs();
    QList<IDBTabButtonBarButton *>& buttons();
    virtual void resizeEvent(QResizeEvent *event);

private:
    IDBTab *currTab_;
    QList<IDBTab *> tabs_;
    QList<IDBTabButtonBarButton *> buttons_;
    IDBTabButtonBarList *buttonBarList_;
    QWidget * tabParent_;

signals:

public slots:

};

#endif // IDBWINDOW_H
