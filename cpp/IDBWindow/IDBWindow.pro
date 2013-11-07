#-------------------------------------------------
#
# Project created by QtCreator 2013-10-29T16:21:09
#
#-------------------------------------------------

QT       += core gui

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

TARGET = IDBWindow
TEMPLATE = app


SOURCES += main.cpp \
    idblayout\idbtab.cpp \
    idblayout\idbtabmapping.cpp \
    idblayout\idbtabtoolbuttonbox.cpp \
    idblayout\idbtabcontent.cpp \
    idblayout\idblayoutmanager.cpp \
    idblayout\idbtemplate.cpp \
    idblayout\idbdeal.cpp \
    idblayout\idbtabbuttonbarbutton.cpp \
    idblayout/idbtabbuttonbarlist.cpp \
    idblayout/idblayoutwindow.cpp \
    idblayout/idbdrag.cpp

HEADERS  += \
    idblayout\idbtab.h \
    idblayout\idbtabmapping.h \
    idblayout\idbtabtoolbuttonbox.h \
    idblayout\idbtabcontent.h \
    idblayout\idblayoutmanager.h \
    idblayout\idbtemplate.h \
    idblayout\idbdeal.h \
    idblayout\idbtabbuttonbarbutton.h \
    idblayout/idbtabbuttonbarlist.h \
    idblayout/idblayoutwindow.h \
    idblayout/idbdrag.h

FORMS    += \
    idblayout\idbtemplate.ui \
    idblayout\idbdeal.ui \
    idblayout\idbtab.ui

RESOURCES += \
    resouce/resource.qrc
