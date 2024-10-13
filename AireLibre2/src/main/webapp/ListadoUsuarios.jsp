<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Aire Libre - Listado de Miembros</title>
    <!-- Link to global styles -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/styles.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/stylesListadoUsuarios.css">
   
</head>
<body class="Listado">
    <%@ include file="/WEB-INF/views/navbar.jsp" %>
    <%@ include file="/WEB-INF/views/LoginRegister.jsp" %>

    <!-- Containers para listado de usuarios -->
    <div id="ListadoUsuarios">
        <div class="container">
            <header>
            </header>
            <main>
                <h2>Listado de Miembros</h2>
                <div class="table-container">
                    <table id="personTable" class="table table-striped">
                        <thead>
                            <tr>
                                <th>Foto</th>
                                <th>Nombre</th>
                                <th>Apellido</th>
                                <th>Nickname</th>
                            </tr>
                        </thead>
                        <tbody>
                            <!-- Inserto las filas con usuarios dinamicamente -->
                        </tbody>
                    </table>
                </div>
            </main>
        </div>
    </div>

    <!-- Scripts -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/ListarUsuarios.js"></script>
    <script src="${pageContext.request.contextPath}/js/CheckEntrenador.js"></script>
    <script src="${pageContext.request.contextPath}/js/ExistenciaNickname.js"></script>
    <script src="${pageContext.request.contextPath}/js/Login.js"></script>
    
        <footer class="bg-dark text-white text-center py-3 fixed-bottom">
		<%@ include file="/WEB-INF/views/footer.jsp" %>
    </footer>
</body>
</html>