#include <QColorDialog>

QColor color = QColorDialog::getColor(Qt::red,
    this,
    "Choose Color",
    QColorDialog::ShowAlphaChannel
);

if (color.isValid()) 
{
    qDebug() << color;
}

class SVPicker : public QWidget
{
    // Draw HSV square
    // Handle mouse
};

class HueSlider : public QWidget
{
    // Draw rainbow bar
    // Handle mouse
};

class ColorPickerWidget : public QWidget
{
    // Connect everything
    // Show selected color preview
};

int main(int argc, char *argv[])
{
    QApplication app(argc, argv);

    ColorPickerWidget picker;
    picker.show();

    return app.exec();
}
