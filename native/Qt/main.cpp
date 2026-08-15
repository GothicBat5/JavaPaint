#include <QApplication>
#include <QMainWindow>
#include <QPainter>
#include <QMouseEvent>
#include <QColorDialog>
#include <QMenuBar>
#include <QAction>

class PaintWidget : public QWidget {
    Q_OBJECT

public:
    PaintWidget(QWidget *parent = nullptr) : QWidget(parent), currentColor(Qt::black) 
    {
        setAttribute(Qt::WA_StaticContents);
    }

    void setColor(const QColor &color) 
    {
        currentColor = color;
    }

    void clearCanvas() 
    {
        image.fill(Qt::white);
        update();
    }

protected:
    void mousePressEvent(QMouseEvent *event) override 
    {

        if (event->button() == Qt::LeftButton) 
        {
            lastPoint = event->pos();
            drawing = true;
        }
    }

    void mouseMoveEvent(QMouseEvent *event) override 
    {
        if ((event->buttons() & Qt::LeftButton) && drawing) 
        {
            drawLineTo(event->pos());
        }
    }

    void mouseReleaseEvent(QMouseEvent *event) 
    override 
    {
        if (event->button() == Qt::LeftButton && drawing) 
        {
            drawLineTo(event->pos());
            drawing = false;
        }
    }

    void paintEvent(QPaintEvent *event) 
    override 
    {
        QPainter painter(this);
        QRect dirtyRect = event->rect();
        painter.drawImage(dirtyRect, image, dirtyRect);
    }

    void resizeEvent(QResizeEvent *event) 
    override 
    {
        if (width() > image.width() || height() > image.height()) 
        {
            int newWidth = qMax(width(), image.width());
            int newHeight = qMax(height(), image.height());
            resizeImage(&image, QSize(newWidth, newHeight));
            update();
        }
        QWidget::resizeEvent(event);
    }

private:
    void drawLineTo(const QPoint &endPoint) 
    {
        QPainter painter(&image);
        painter.setPen(QPen(currentColor, 3, Qt::SolidLine, Qt::RoundCap, Qt::RoundJoin));
        painter.drawLine(lastPoint, endPoint);
        update(QRect(lastPoint, endPoint).normalized().adjusted(-2, -2, 2, 2));
        lastPoint = endPoint;
    }

    void resizeImage(QImage *image, const QSize &newSize) 
    {
        if (image->size() == newSize) return;

        QImage newImage(newSize, QImage::Format_RGB32);
        newImage.fill(Qt::white);
        QPainter painter(&newImage);
        painter.drawImage(QPoint(0, 0), *image);
        *image = newImage;
    }

    QImage image = QImage(400, 400, QImage::Format_RGB32);
    QPoint lastPoint;
    QColor currentColor;
    bool drawing = false;
};

class PaintApp : public QMainWindow {
    Q_OBJECT

public:
    PaintApp() 
    {
        paintWidget = new PaintWidget(this);
        setCentralWidget(paintWidget);

        QMenu *fileMenu = menuBar()->addMenu("&File");
        QAction *clearAction = fileMenu->addAction("Clear");
        connect(clearAction, &QAction::triggered, paintWidget, &PaintWidget::clearCanvas);

        QMenu *colorMenu = menuBar()->addMenu("&Color");
        QAction *chooseColorAction = colorMenu->addAction("Choose Color");
        connect(chooseColorAction, &QAction::triggered, this, &PaintApp::chooseColor);
    }

private slots:
    void chooseColor() 
    {
        QColor color = QColorDialog::getColor(Qt::black, this);
        if (color.isValid()) 
        {
            paintWidget->setColor(color);
        }
    }

private:
    PaintWidget *paintWidget;
};

int main(int argc, char *argv[]) 
{
    QApplication app(argc, argv);
    PaintApp window;
    window.resize(600, 600);
    window.show();
    return app.exec();
}
