<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Valorar Curso - edEXT</title>
  
  <!-- Bootstrap 5 CSS -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
  
  <!-- Bootstrap Icons -->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">

  <!-- Hoja de estilos personalizada -->
  <link rel="stylesheet" href="<%= request.getContextPath() %>/Styles/StyleValoracion.css">
</head>
<body class="bg-light">

  <div class="container my-5">
    <div class="card shadow-sm border-0 p-4 rounded-3">
      <h3 class="card-title fw-bold mb-4 text-primary">Valoración del Curso</h3>

      <!-- 1. RESUMEN GENERAL Y DESGLOSE DE PUNTUACIONES -->
      <div class="row align-items-center mb-4">
        
        <!-- Promedio general de estrellas -->
        <div class="col-md-4 text-center border-end pe-md-4 mb-4 mb-md-0">
          <h1 class="display-2 fw-bold text-dark mb-0">3.3</h1>
          <div class="text-warning my-2 fs-3">
            <i class="bi bi-star-fill"></i>
            <i class="bi bi-star-fill"></i>
            <i class="bi bi-star-fill"></i>
            <i class="bi bi-star-fill"></i>
            <i class="bi bi-star-half"></i>
          </div>
          <p class="text-muted small mb-0">9,955 valoraciones totales</p>
        </div>

        <!-- Barras de progreso por estrellas (Estilo Google Play) -->
        <div class="col-md-8 ps-md-4">
          
          <!-- 5 Estrellas -->
          <div class="d-flex align-items-center mb-2">
            <span class="small fw-semibold text-secondary" style="width: 50px;">5 star</span>
            <div class="progress flex-grow-1 mx-2" style="height: 8px;">
              <div class="progress-bar bg-success" role="progressbar" style="width: 66%;" aria-valuenow="66" aria-valuemin="0" aria-valuemax="100"></div>
            </div>
            <span class="small text-muted text-end" style="width: 45px;">6,556</span>
          </div>

          <!-- 4 Estrellas -->
          <div class="d-flex align-items-center mb-2">
            <span class="small fw-semibold text-secondary" style="width: 50px;">4 star</span>
            <div class="progress flex-grow-1 mx-2" style="height: 8px;">
              <div class="progress-bar bg-success" role="progressbar" style="width: 10%;" aria-valuenow="10" aria-valuemin="0" aria-valuemax="100"></div>
            </div>
            <span class="small text-muted text-end" style="width: 45px;">888</span>
          </div>

          <!-- 3 Estrellas -->
          <div class="d-flex align-items-center mb-2">
            <span class="small fw-semibold text-secondary" style="width: 50px;">3 star</span>
            <div class="progress flex-grow-1 mx-2" style="height: 8px;">
              <div class="progress-bar bg-warning" role="progressbar" style="width: 5%;" aria-valuenow="5" aria-valuemin="0" aria-valuemax="100"></div>
            </div>
            <span class="small text-muted text-end" style="width: 45px;">348</span>
          </div>

          <!-- 2 Estrellas -->
          <div class="d-flex align-items-center mb-2">
            <span class="small fw-semibold text-secondary" style="width: 50px;">2 star</span>
            <div class="progress flex-grow-1 mx-2" style="height: 8px;">
              <div class="progress-bar bg-warning" role="progressbar" style="width: 3%;" aria-valuenow="3" aria-valuemin="0" aria-valuemax="100"></div>
            </div>
            <span class="small text-muted text-end" style="width: 45px;">194</span>
          </div>

          <!-- 1 Estrella -->
          <div class="d-flex align-items-center mb-2">
            <span class="small fw-semibold text-secondary" style="width: 50px;">1 star</span>
            <div class="progress flex-grow-1 mx-2" style="height: 8px;">
              <div class="progress-bar bg-danger" role="progressbar" style="width: 9%;" aria-valuenow="9" aria-valuemin="0" aria-valuemax="100"></div>
            </div>
            <span class="small text-muted text-end" style="width: 45px;">869</span>
          </div>

        </div>
      </div>

      <hr class="my-4 text-muted">

      <!-- 2. FORMULARIO INTERACTIVO PARA EL ESTUDIANTE -->
      <div class="bg-light p-3 rounded-3 border">
        <h5 class="fw-bold mb-1">Tu valoración</h5>
        <p class="text-muted small mb-3">Selecciona la puntuación que deseas asignar a este curso (solo una vez).</p>
        
        <form action="ValorarCurso" method="POST">
          <input type="hidden" name="idCurso" value="123">
          
          <div class="d-flex align-items-center gap-3 mb-3">
            <div class="star-rating fs-2">
              <input type="radio" id="star5" name="puntaje" value="5" required />
              <label for="star5" title="5 estrellas"><i class="bi bi-star-fill"></i></label>
              
              <input type="radio" id="star4" name="puntaje" value="4" />
              <label for="star4" title="4 estrellas"><i class="bi bi-star-fill"></i></label>
              
              <input type="radio" id="star3" name="puntaje" value="3" />
              <label for="star3" title="3 estrellas"><i class="bi bi-star-fill"></i></label>
              
              <input type="radio" id="star2" name="puntaje" value="2" />
              <label for="star2" title="2 estrellas"><i class="bi bi-star-fill"></i></label>
              
              <input type="radio" id="star1" name="puntaje" value="1" />
              <label for="star1" title="1 estrella"><i class="bi bi-star-fill"></i></label>
            </div>
          </div>

          <button type="submit" class="btn btn-primary px-4 fw-semibold">Enviar Valoración</button>
        </form>
      </div>

    </div>
  </div>

  <!-- Bootstrap 5 JavaScript Bundle -->
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>