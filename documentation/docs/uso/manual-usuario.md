# Manual de Usuario

Esta sección presenta el flujo básico de uso de la aplicación para el etiquetado de objetos.



<style>
.screenshot {
  display: block;
  max-width: 900px;  
  width: 100%;      
  height: auto;      /
  margin: 0 auto;   
  border-radius: 12px;
  box-shadow: 0 6px 22px rgba(0,0,0,.12);
}


.gallery {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 16px;
  align-items: start;
}
</style>

---

## 1. Solicitar permisos

La aplicación requiere permisos para **cámara**, **galería** y **ubicación** (opcional).

<div class="gallery">
  <img class="screenshot" 
       src="../img/uso/1-permisos.jpeg" 
       alt="Permisos: diálogo del sistema (1)" 
       style="width:300px; height:auto; border-radius:10px; box-shadow:0 4px 14px rgba(0,0,0,.15);" />

  <img class="screenshot" 
       src="../img/uso/2-permisos.jpeg" 
       alt="Permisos: diálogo del sistema (2)" 
       style="width:300px;  height:auto; border-radius:10px; box-shadow:0 4px 14px rgba(0,0,0,.15);" />
</div>


---

## 2. Pantalla de inicio

Pantalla inicial con acceso al botón **“Empezar”**.

<img class="screenshot" src="../img/uso/3-pantalla-inicio.jpeg" alt="Pantalla de inicio con botón Empezar" style="width:300px;  height:auto; border-radius:10px; box-shadow:0 4px 14px rgba(0,0,0,.15);">

---

## 3. Menú de opciones

Desde aquí se accede a todas las funcionalidades principales.

<img class="screenshot" src="../img/uso/4-menu-opciones.jpeg" alt="Menú de opciones principal" style="width:300px;  height:auto; border-radius:10px; box-shadow:0 4px 14px rgba(0,0,0,.15);">

---

## 4. Historial de detecciones y etiquetados

Se puede consultar el historial de imágenes previamente **etiquetadas** y **texto detectado**.

<img class="screenshot" src="../img/capturas/historial.jpeg" alt="Pantalla de historial de detecciones" style="width:300px;  height:auto; border-radius:10px; box-shadow:0 4px 14px rgba(0,0,0,.15);">

---

## 5. Etiquetado desde galería

Pantalla donde se selecciona una imagen y se etiqueta con ML Kit.

<img class="screenshot" src="../img/uso/6-etiquetado.jpeg" alt="Pantalla de etiquetado con ML Kit" style="width:300px;  height:auto; border-radius:10px; box-shadow:0 4px 14px rgba(0,0,0,.15);">

### Ejemplos

1) **Seleccionar la imagen** a etiquetar o de la cual se extraerá texto:

<img class="screenshot" src="../img/capturas/seleccion.jpeg" alt="Selección de imagen desde la galería" style="width:300px;  height:auto; border-radius:10px; box-shadow:0 4px 14px rgba(0,0,0,.15);">

2) **Imagen para extraer texto** (OCR):

<img class="screenshot" src="../img/capturas/texto.jpeg" alt="Ejemplo de imagen para reconocimiento de texto" style="width:300px;  height:auto; border-radius:10px; box-shadow:0 4px 14px rgba(0,0,0,.15);">

3) **Imagen para etiquetar** (clasificación/etiquetado):

<img class="screenshot" src="../img/capturas/etiquetados.jpeg" alt="Ejemplo de resultados de etiquetado de imagen" style="width:300px;  height:auto; border-radius:10px; box-shadow:0 4px 14px rgba(0,0,0,.15);">
