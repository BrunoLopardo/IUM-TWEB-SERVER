<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html ng-app="appRipetizioni" ng-controller="mainCtrl">
    <head>
        <title>App Ripetizioni</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <base href="<%=request.getContextPath()%>/">

        <link rel="shortcut icon" href="">
        
        <!-- bootstrap css -->
        <link rel="stylesheet" type="text/css" href="css/lib/bootstrap.min.css">
        
        <!-- custom css -->
        <link rel="stylesheet" type="text/css" href="css/main.css">

        <!-- angular -->
        <script src="js/lib/angular.min.js"></script>
        <script src="js/lib/angular-route.min.js"></script>

        <!-- custom js -->
        <script src="js/app.js"></script>
        <script src="componenti/paginazione/paginazione.component.js"></script>
        <script src="js/home-controller.js"></script>
        <script src="js/corsi-controller.js"></script>
        <script src="js/docenti-controller.js"></script>
        <script src="js/prenotazioni-controller.js"></script>
        <script src="js/docenze-controller.js"></script>
        <script src="js/modifica-account-controller.js"></script>
        <script src="js/prenotazioni-utente-controller.js"></script>
        <script src="js/utenti-controller.js"></script>
        
        <!-- jquery e bootstrap -->
        <script src="js/lib/jquery-3.3.1.slim.min.js"></script>
        <script src="js/lib/popper.min.js"></script>
        <script src="js/lib/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="notify"></div>
        <header>
            <nav class="navbar navbar-expand-md navbar-light bg-light">
                <a class="navbar-brand" href="#">TutorHUB</a>
                <button class="navbar-toggler collapsed" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="navbar-collapse collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav mr-auto">
                        <li class="nav-item active">
                            <a class="nav-link" href="./">Home <span class="sr-only">(current)</span></a>
                        </li>      
                        <li class="nav-item dropdown" ng-if="login_data.isAdmin">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarAdminDropdown" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">Pannello di controllo</a>
                            <div class="dropdown-menu" aria-labelledby="navbarAdminDropdown">
                                <a class="dropdown-item" href="corsi">Corsi</a>
                                <a class="dropdown-item" href="docenti">Docenti</a>
                                <a class="dropdown-item" href="docenze">Docenze</a>
                                <a class="dropdown-item" href="utenti">Utenti</a>
                                <a class="dropdown-item" href="prenotazioni">Prenotazioni</a>
                            </div>
                        </li>
                    </ul>
                    <ul class="navbar-nav ml-auto">
                        <li class="nav-item dropdown" ng-if="login_data.loggedIn">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                {{login_data.username}}&nbsp;</a>
                            <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdown">
                                <a class="dropdown-item" href="modificaAccount">Modifica Account</a>
                                <a class="dropdown-item" href="prenotazioniUtente">Prenotazioni</a>
                                <div class="dropdown-divider"></div>
                                <a class="dropdown-item" ng-click="logout()">Logout</a>
                            </div>
                        </li>
                        <button class="btn btn-outline-success my-2 my-sm-0" type="button" data-toggle="modal" data-target="#loginModal" ng-if="!login_data.loggedIn">Login / Signup</button>
                    </ul>  
                </div>
            </nav>
        </header>

        <aside>
            <div class="modal fade" id="loginModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalCenterTitle" aria-hidden="true">
                <div class="modal-dialog" role="document">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="exampleModalCenterTitle">Login / Signup</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                        
                        <div class="modal-body">
                            <div id="loginModalErrorMessage" class="alert alert-danger" role="alert" ng-if="loginErrorOccurred">
                                {{loginErrorMessage}}
                            </div>
                            <div class="form-group">
                                <label for="usernameInput">Username</label>
                                <input type="text" class="form-control" id="usernameInput" placeholder="Inserisci Username" ng-model="loginModalUsername">
                            </div>
                            <div class="form-group">
                                <label for="passwordInput">Password</label>
                                <input type="password" class="form-control" id="passwordInput" placeholder="Inserisci Password" ng-model="loginModalPassword">
                            </div>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" ng-click="signup()" ng-disabled="!(loginModalUsername && loginModalPassword)">Signup</button>
                            <button type="button" class="btn btn-primary" ng-click="login()" ng-disabled="!(loginModalUsername && loginModalPassword)">Login</button>
                        </div>
                    </div>
                </div>
            </div>
        </aside>

        <section>
            <div ng-view></div>
        </section>
    </body>
</html>

