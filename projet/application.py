import sys
from PyQt5.QtGui import QImage, QPainter, QPen, QPixmap
from PyQt5.QtWidgets import QApplication, QLabel, QMainWindow, QAction, QFileDialog, QVBoxLayout, QWidget
from PyQt5.QtCore import Qt, QPoint

class MainWindow(QMainWindow):
    def __init__(self):
        super().__init__()

        # Créer un widget label pour afficher l'image
        self.label = QLabel(self)

        # Créer un widget label pour afficher le nombre de points cliqués
        self.points_label = QLabel('Points cliqués: 0', self)

        # Créer un layout vertical pour les deux labels
        layout = QVBoxLayout()
        layout.addWidget(self.label)
        layout.addWidget(self.points_label)

        # Créer un widget pour contenir le layout
        widget = QWidget(self)
        widget.setLayout(layout)
        self.setCentralWidget(widget)

        # Créer une action pour ouvrir une image
        open_action = QAction('Ouvrir', self)
        open_action.setShortcut('Ctrl+O')
        open_action.triggered.connect(self.open_image)
        self.addAction(open_action)

        # Initialiser la variable de dessin et le compteur de points
        self.drawing = False
        self.last_point = QPoint()
        self.point_count = 0

    def open_image(self):
        # Ouvrir une boîte de dialogue pour sélectionner une image
        filename, _ = QFileDialog.getOpenFileName(self, 'Ouvrir une image', '', 'Images (*.png *.jpeg *.jpg *.bmp)')

        if filename:
            # Charger l'image dans un QPixmap et afficher dans le label
            image = QImage(filename)
            self.label.setPixmap(QPixmap.fromImage(image))

    def mousePressEvent(self, event):
        if event.button() == Qt.LeftButton:
            # Enregistrer le dernier point de clic de souris
            self.drawing = True
            self.last_point = event.pos()
            # Mettre à jour le nombre de points cliqués
            self.point_count += 1
            self.points_label.setText(f'Points cliqués: {self.point_count}')

    def mouseMoveEvent(self, event):
        if (event.buttons() & Qt.LeftButton) and self.drawing:
            # Dessiner une ligne depuis le dernier point de clic de souris
            painter = QPainter(self.label.pixmap())
            painter.setPen(QPen(Qt.red, 2, Qt.SolidLine))
            painter.drawLine(self.last_point, event.pos())
            self.last_point = event.pos()
            self.label.update()

    def mouseReleaseEvent(self, event):
        if event.button() == Qt.LeftButton:
            self.drawing = False

if __name__ == '__main__':
    app = QApplication(sys.argv)
    window = MainWindow()
    window.show()
    sys.exit(app.exec_())
