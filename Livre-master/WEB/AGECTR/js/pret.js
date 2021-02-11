$(document).ready(function() {

  if ($(".pret").parents().hasClass('bg-danger')) {
    alert("hello");
  }



  $(document).on("click", ".pret", function() {

    var id = $(this).closest('tr').attr('id');
    var statut = $(this).closest('tr').hasClass('bg-danger');
    $("#id").val(id);
    var etat;
    if (statut == true) {
      etat = 0;
    } else {
      etat = -3;
    }
    $("#statut").val(etat);

    if (statut == true) {
      $("#message").text("Est-ce que le livre est prêt ?")
      $("#titre").text("Livre prêt ?");
    } else {
      $("#message").text("Êtes-vous certain de vouloir rendre le livre disponible ? ");
      $("#titre").text("Rendre livre disponible");
    }

    $("#pretModal").modal("show");
  });

  $(document).on("click", "#pret", function() {

    var id = $("#id").val();
    var statut = $("#statut").val();

    $.ajax({
      url: "php/pret.php",
      method: "POST",
      data: {
        id: id,
        etat: etat
      },
      success: function(data) {
        if (data == "success") {
          $('#tblReservation').DataTable().ajax.reload();
        } else {
          alert(data);
        }
      }
    });
  });

});