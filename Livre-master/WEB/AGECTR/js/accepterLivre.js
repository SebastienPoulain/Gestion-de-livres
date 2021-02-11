$(document).ready(function() {

  $(document).on("click", ".acceptLivre", function() {

    var livre = $(event.target)
      .closest("tr")
      .find($("td:nth-child(1)"))
      .text();

    $("#userName").text(livre + " ?");

    var id = $(this).closest('tr').attr('id');

    $("#idAccepter").val(id);


    $("#accepterModal").modal("show");

  });

  $(document).on("click", "#accepter", function() {

    var id = $("#idAccepter").val();

    $.ajax({
      url: "php/accepterLivre.php",
      method: "POST",
      data: {
        id: id
      },
      success: function(data) {
        if (data == "success") {
          alert("Le livre a été validé avec succès");
          $("#accepterModal").modal("hide");
          $('#tblLivre').DataTable().ajax.reload();
        } else {
          alert(data);
        }
      }
    });

  });

});