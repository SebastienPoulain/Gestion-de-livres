
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <meta http-equiv="x-ua-compatible" content="ie=edge">

  <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.0.13/css/all.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/twitter-bootstrap/4.1.3/css/bootstrap.css">
  <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.20/css/dataTables.bootstrap4.min.css">
  <link rel="stylesheet" href="css/adminlte.min.css">

  <script src="https://code.jquery.com/jquery-3.3.1.js" charset="utf-8"></script>
  <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.4.1/js/bootstrap.min.js" charset="utf-8"></script>
  <script src="//cdn.datatables.net/1.10.20/js/jquery.dataTables.min.js" charset="utf-8"></script>
  <script src="https://cdn.datatables.net/1.10.20/js/dataTables.bootstrap4.min.js" charset="utf-8"></script>
  <script src="js/adminlte.min.js"></script>
  <script src="js/main.js"></script>
  <script src="js/pullUsers.js" charset="utf-8"></script>
  <script src="js/accepter.js" charset="utf-8"></script>
  <script src="js/refuser.js" charset="utf-8"></script>

  <style>
    td,th{
      text-align: center;
    }
    .acceptUser{
    padding:4%;
  }
    .deleteUser{
        padding:2%;
    }
  </style>

</head>

<body class="hold-transition sidebar-mini">

  <div class="wrapper">

    <nav class="main-header navbar navbar-expand navbar-white navbar-light">

      <ul class="navbar-nav">
        <li class="nav-item">
          <a class="nav-link" data-widget="pushmenu" href="#"><i class="fas fa-bars"></i></a>
        </li>
      </ul>
    </nav>

    <aside class="main-sidebar sidebar-dark-primary elevation-4">

      <a style="cursor:pointer" href="http://206.167.140.56:8118/420617ri_equipe-2/web/app_dev_cegep.php" class="brand-link">
        <img src="img/logo.png" alt="Logo AGECTR" class="brand-image elevation-3" style="opacity: .8">
        <span class="brand-text font-weight-light"><b>AGECTR</b></span>
      </a>

      <div class="sidebar">

        <nav id="menuDashboard" class="mt-2">
          <ul class="nav nav-pills nav-sidebar flex-column" data-widget="treeview" role="menu" data-accordion="false">
            <li class="nav-item has-treeview">
            <a href="#" class="nav-link">
              <i class="nav-icon far  fa-user-circle"></i>
              <p>
                Comptes
                <i class="right fas fa-angle-left"></i>
              </p>
            </a>
            <ul class="nav nav-treeview" style="display: none;">
              <li style="background-color:gray !important" class="nav-item">
                <a href="activerCompte.php" class="nav-link">
                  <i class="fas fa-user-plus nav-icon"></i>
                  <p>Activer comptes</p>
                </a>
              </li>
              <li class="nav-item">
                <a href="CreationCompte.php" class="nav-link">
                  <i class="fas fa-user-plus nav-icon"></i>
                  <p>Création de comptes</p>
                </a>
                </ul>
              </li>
            <li class="nav-item has-treeview">
            <a href="#" class="nav-link">
              <i class="nav-icon fas fa-book"></i>
              <p>
               Concessions
               <i class="right fas fa-angle-left"></i>
             </p>
           </a>
           <ul class="nav nav-treeview" style="display: none;">
             <li class="nav-item">
               <a href="activerConcession.php" class="nav-link">
                 <i class="fas fa-plus-circle nav-icon"></i>
                 <p>Activer concession</p>
                </a>
              </li>
              <li class="nav-item">
                <a href="modifierConcession.php" class="nav-link">
                  <i class="fas fa-edit nav-icon"></i>
                  <p>Modifier concession</p>
                 </a>
               </li>
               <li  class="nav-item">
                 <a href="AjouterLocalisation.php" class="nav-link">
                   <i class="fas fa-map-marker-alt nav-icon"></i>
                   <p>Ajouter localisation</p>
                  </a>
                </li>
            </ul>
          </li>
          <li class="nav-item has-treeview">
          <a href="#" class="nav-link">
            <i class="nav-icon fas fa-book"></i>
            <p>
             Livres
             <i class="right fas fa-angle-left"></i>
           </p>
         </a>
         <ul class="nav nav-treeview" style="display: none;">
           <li class="nav-item">
             <a href="changerImg.php" class="nav-link">
               <i class="far fa-image nav-icon"></i>
               <p>Ajouter image</p>
              </a>
            </li>
            <li class="nav-item">
              <a href="activerLivre.php" class="nav-link">
                <i class="fas fa-plus-circle nav-icon"></i>
                <p>Accepter livre</p>
               </a>
             </li>
          </ul>
        </li>
        <li class="nav-item has-treeview">
        <a href="#" class="nav-link">
          <i class="nav-icon fa fa-wpforms"></i>
          <p>
           Réservation
           <i class="right fas fa-angle-left"></i>
         </p>
       </a>
       <ul class="nav nav-treeview" style="display: none;">
         <li class="nav-item">
           <a href="listeReservation.php" class="nav-link">
             <i class="fas fa-list nav-icon"></i>
             <p>Liste des réservations</p>
            </a>
          </li>
        </ul>
      </li>
      <li class="nav-item has-treeview">
      <a href="#" class="nav-link">
        <i class="nav-icon fa fa-wpforms"></i>
        <p>
         Donations
         <i class="right fas fa-angle-left"></i>
       </p>
     </a>
     <ul class="nav nav-treeview" style="display: none;">
       <li class="nav-item">
         <a href="donnation.php" class="nav-link">
           <i class="fas fa-list nav-icon"></i>
           <p>liste des donations</p>
          </a>
        </li>
      </ul>
    </li>
        </nav>
      </div>
    </aside>

    <div class="content-wrapper">
      <section class="content-header">
        <div class="container-fluid col-md-12">
          <h1 style="text-align:center !important;float:none;font-weight:bold;" class="card-title">Activation des comptes</h1>
          <br>
          <br>
        </div>
      </section>

      <section class="content">

    <div class="col-sm-12">
      <table id="tblValidationComptes" class="display table table-striped table-bordered" style="width:100%">
        <thead>
          <tr>
            <th>Matricule</th>
            <th>Nom</th>
            <th>lastName</th>
            <th>Téléphone</th>
            <th>Email</th>
            <th>Date de création</th>
            <th>Statut</th>
            <th>Actions</th>
          </tr>
        </thead>
      </table>
    </div>
    </section>

    <div id="accepterModal" class="modal fade" role="dialog">
      <div class="modal-dialog text-center">
        <div class="modal-content text-center">
          <div style="margin:0px auto;border:none;" class="modal-header text-center">
            <h4 class="modal-title">Accepter le compte</h4>
          </div>
          <div style="border:none" class="modal-body text-center">
            <label>Êtes-vous certain de vouloir accepter</label> <span style="font-weight:bold;text-align:center;" id="userName"></span>

          </div>
          <div style="border:none" class="modal-footer">
            <input id="idAccepter" type="hidden"/>
            <button type="button" id="fermer" class="btn btn-secondary float-right" data-dismiss="modal">Annuler</button>
            <input type="button" name="accepter" class="btn btn-success" id="accepter" value="Accepter" class="float-right" />
          </div>
        </div>
      </div>
    </div>

    <div id="supprimerModal" class="modal fade" role="dialog">
      <div class="modal-dialog text-center">
        <div class="modal-content text-center">
          <div style="margin:0px auto;border:none;" class="modal-header text-center">
            <h4 class="modal-title">Refuser le compte</h4>
          </div>
          <div style="border:none" class="modal-body text-center">
            <label>Êtes-vous certain de vouloir refuser</label> <span style="font-weight:bold;text-align:center;" id="userName2"></span>
          </div>
          <div style="border:none" class="modal-footer">
            <input id="idSupprimer" type="hidden"/>
            <button type="button"  class="btn btn-secondary float-right" data-dismiss="modal">Annuler</button>
            <input type="button" name="supprimer" class="btn btn-success" id="supprimer" value="Refuser" class="float-right" />
          </div>
        </div>
      </div>
    </div>
</div>

</body>
</html>
