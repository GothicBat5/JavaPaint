#include "filesaver.h"
#include <QFile>
#include <QPainter>

bool FileSaver::saveImage(const QImage &image, const QString &filePath) 
{
    return image.save(filePath, "PNG");
}
