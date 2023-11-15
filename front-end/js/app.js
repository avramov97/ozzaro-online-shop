let k = window.location.href;
let loggedIn = false;
let hiddenFlag = true;

let constants = {
    serviceUrl: "http://127.0.0.1:8000"
};

function loginMain()
{
    let username = $('#username').val();
    let password = $('#password').val();

    $.ajax({
        type: 'POST',
        url: constants.serviceUrl + '/login',
        headers: {
            'Content-Type': 'application/json'
        },
        data: JSON.stringify({
            "username": username,
            "password": password
        })
    }).done((data, status, request) => {
        let authToken = data.split('Bearer ')[1];
        app.authorizationService.saveCredentials(authToken);
        loadNavbar();
        loggedIn = true;
        window.location.href = '#/';
    }).fail((err) =>
    {
        alert('Error: ' + err);
        if(err.status == 401 || err.status == 403)
        {
            document.getElementById("error_login").hidden = false;
        }
    });
}

function login()
{
    let username = $('#username').val();
    let password = $('#password').val();

    $.ajax({
        type: 'POST',
        url: constants.serviceUrl + '/login',
        headers: {
            'Content-Type': 'application/json'
        },
        data: JSON.stringify({
            "username": username,
            "password": password
        })
    }).done((data, status, request) => {
        let authToken = data.split('Bearer ')[1];
        app.authorizationService.saveCredentials(authToken);
        loadNavbar();
    }).fail((err) =>
    {
        alert('Error: ' + err);
        if(err.status == 401 || err.status == 403)
        {
            document.getElementById("error_login").hidden = false;
        }
    });
}

function loginAndAddToCart(elem, inputSize)
{
    let username = $('#username').val();
    let password = $('#password').val();

    $.ajax({
        type: 'POST',
        url: constants.serviceUrl + '/login',
        headers: {
            'Content-Type': 'application/json'
        },
        data: JSON.stringify({
            "username": username,
            "password": password
        })
    }).done((data, status, request) => {
        let authToken = data.split('Bearer ')[1];
        app.authorizationService.saveCredentials(authToken);
        loadNavbar();
        $("#login-modal").modal('hide');
        // let inputSize = $('#' + sizeSelect).val();

        $.ajax({
            type: 'POST',
            url: constants.serviceUrl + '/save-order',
            headers: {
                'Content-Type': 'application/json',
                //  'Authorization': app.authorizationService.getCredentials()
            },
            data: JSON.stringify({
                "sweatshirt": elem,
                "username": app.authorizationService.getUsername(),
                "size": inputSize
            })
        }).done((data, request) =>
        {
            let cartSize = parseInt($("#cart-size").text());
            $("#cart-size").text(cartSize+1);
            $("#cart-success").modal();
        }).fail((err) =>
        {
            console.log(err);
            alert(err.responseText);
            document.getElementById("error_register").hidden = false;
        });

    }).fail((err) =>
    {
        alert('Error: ' + err);
        if(err.status == 401 || err.status == 403)
        {
            document.getElementById("error_login").hidden = false;
        }
    });
}

function loadCartSizeForModerator()
{
    $.ajax({
        type: 'GET',
        url: constants.serviceUrl + '/cart-size?username=' + app.authorizationService.getUsername(),
        headers: {
            'Content-Type': 'application/json'
        }
    }).done((data, request) =>
    {
        loadOrdersSize();
        $("#cart-size").text(data);
    }).fail((err) =>
    {
        console.log(err);
        alert(err.responseText);
        document.getElementById("error_register").hidden = false;
    });
}

function loadCartSizeForUser()
{
    $.ajax({
        type: 'GET',
        url: constants.serviceUrl + '/cart-size?username=' + app.authorizationService.getUsername(),
        headers: {
            'Content-Type': 'application/json'
        }
    }).done((data, request) =>
    {
        $("#cart-size").text(data);
        // alert($("#cart-size").text());
        // alert('set');
    }).fail((err) =>
    {
        console.log(err);
        alert(err.responseText);
        document.getElementById("error_register").hidden = false;
    });
}

function loadOrdersSize()
{
    $.ajax({
        type: 'GET',
        url: constants.serviceUrl + '/waiting-size?username=' + app.authorizationService.getUsername(),
        headers: {
            'Content-Type': 'application/json'
        }
    }).done((data, request) =>
    {
        $("#waiting-size").text(data);
    }).fail((err) =>
    {
        console.log(err);
        alert(err.responseText);
        document.getElementById("error_register").hidden = false;
    });

    $.ajax({
        type: 'GET',
        url: constants.serviceUrl + '/delivered-size?username=' + app.authorizationService.getUsername(),
        headers: {
            'Content-Type': 'application/json'
        }
    }).done((data, request) =>
    {
        $("#delivered-size").text(data);
    }).fail((err) =>
    {
        console.log(err);
        alert(err.responseText);
        document.getElementById("error_register").hidden = false;
    });
}

function loadNavbar()
{
    let username = app.authorizationService.getUsername();
    if (app.authorizationService.getRole() === 'ROOT-ADMIN')
    {
        app.templateLoader.loadTemplate('.navbar-holder', 'navbar-root-admin');
        loadCartSizeForModerator();
    }
    else if (app.authorizationService.getRole() === 'ADMIN')
    {
        app.templateLoader.loadTemplate('.navbar-holder', 'navbar-admin');
        loadCartSizeForModerator();
    }
    else if (app.authorizationService.getRole() === 'MODERATOR')
    {
        app.templateLoader.loadTemplate('.navbar-holder', 'navbar-moderator');
        loadCartSizeForModerator();
    }
    else if (app.authorizationService.getRole() === 'ROLE_USER')
    {
        app.templateLoader.loadTemplate('.navbar-holder', 'navbar-user');
        loadCartSizeForUser();
    }
    else
    {
        app.templateLoader.loadTemplate('.navbar-holder', 'navbar-guest');
    }
}

app.router.on('#/sweatshirts/product', ['id'], function (id)
{
    // loadNavbar();
    $.ajax(
        {
            type: 'GET',
            url: constants.serviceUrl + '/sweatshirts/product?id=' + id,
            headers: {
                'Content-Type': 'application/json',
            }
        }).done((data) =>
        {
        app.templateLoader.loadTemplate('.app', 'sweatshirt', function ()
        {
            let auth = app.authorizationService.getRole();
            let saving = data['oldPrice'] - data['newPrice'];
            let fullSize = '';
            let options = '<option value="">Избери величина</option>';
            $("#sweatshirt-name").html('<b>' + data['name'] + '</b>');
            $("#modal-img").attr("src", data['img1']);
            $("#rating-system").html
            (           '<p class="mt-2 ml-2" id="avg-rating-modal"></p>'
                +       '<input type="radio" name="rating" value="5" id="five-star"><label for="five-star">☆</label>'
                +       '<input type="radio" name="rating" value="4" id="four-star"><label for="four-star">☆</label>'
                +       '<input type="radio" name="rating" value="3" id="three-star"><label for="three-star">☆</label>'
                +       '<input type="radio" name="rating" value="2" id="two-star"><label for="two-star">☆</label>'
                +       '<input type="radio" name="rating" value="1" id="one-star"><label for="one-star">☆</label>'
            );
            $("#order-col").html
            (
                '<form class="needs-validation" novalidate>'
                +       '<div class="input-group form-group">'
                +           '<select class="form-control" id="select-size" required>'
                +           '</select>'
                +           '<div class="invalid-feedback">'
                +               'Избери величина'
                +           '</div>'
                +       '</div>'
                +       '<div class="input-group form-group">'
                +           '<div class="input-group-prepend">'
                +               '<span class="input-group-text"><i class="fas fa-user-tie"></i></span>'
                +           '</div>'
                +           '<input id="name" type="text" class="form-control rounded-right" placeholder="Име и презиме" minlength="5" maxlength="100" required>'
                +           '<div class="invalid-feedback">'
                +               'Името и презимето треба да е над 5 букви'
                +           '</div>'
                +       '</div>'
                +       '<div class="input-group form-group">'
                +           '<div class="input-group-prepend">'
                +               '<span class="input-group-text"><i class="fas fa-phone-alt"></i></span>'
                +           '</div>'
                +           '<input id="mobile-num" type="tel" class="form-control rounded-right" placeholder="Мобилен број" minlength="3" maxlength="100" required>'
                +           '<div class="invalid-feedback">'
                +               'Мобилниот број треба да е на 5 бројки'
                +           '</div>'
                +       '</div>'
                +       '<div class="input-group form-group">'
                +           '<div class="input-group-prepend">'
                +               '<span class="input-group-text"><i class="fas fa-city"></i></span>'
                +           '</div>'
                +           '<input id="city" type="text" class="form-control rounded-right" placeholder="Град" minlength="3" maxlength="100" required>'
                +           '<div class="invalid-feedback">'
                +               'Името на град треба да е над 3 букви'
                +           '</div>'
                +       '</div>'
                +       '<div class="input-group form-group">'
                +           '<div class="input-group-prepend">'
                +               '<span class="input-group-text"><i class="fas fa-map-marked-alt"></i></span>'
                +          '</div>'
                +           '<input id="address" type="text" class="form-control rounded-right" placeholder="Адреса" minlength="3" maxlength="100" required>'
                +           '<div class="invalid-feedback">'
                +              'Адресата треба да е над 3 букви'
                +           '</div>'
                +       '</div>'
                +       '<div class="input-group form-group">'
                +           '<div class="input-group-prepend">'
                +               '<span class="input-group-text"><i class="fas fa-envelope"></i></span>'
                +           '</div>'
                +           '<input id="mail" type="email" class="form-control rounded-right" placeholder="Е-маил" maxlength="100" required>'
                +           '<div class="invalid-feedback">'
                +               'Воведете валиден меил'
                +           '</div>'
                +       '</div>'
                +       '<button type="click" id="submit-button" class="btn btn-success btn-block">Порачај</button>'
                +       '</form>'
            );

            $("#intro-sweatshirt-name").text(data['name']);
            // $("#order-modal").modal();
            $("#add-to-cart").html('<button  id="add-to-cart-button" class="btn btn-dark btn-block">Додади во количката</button>');
            $("#old-price").text(data['oldPrice'] + 'ден.');
            $("#new-price").html('<b>' + data['newPrice'] + 'ден.</b>');
            $("#saving").text('Вие заштедувате ' + saving + ' денари');
            $("#lead").text(data['description']);

            for(let size of data['sizes'])
            {
                fullSize += size;

                if(size != 'X')
                {
                    $('#select-size').append('<div class="mr-3 p-2 bg-dark text-white" style="font-size: 25px !important;"><b>' + fullSize + '</b></div>');
                    options += '<option value="' + fullSize + '">' + fullSize + '</option>';
                    fullSize = '';
                }
            }

            $('#select-size').html(options);

            (function()
            {
                'use strict';
                var forms = document.getElementsByClassName('needs-validation');
                var validation = Array.prototype.filter.call(forms, function(form) {
                    form.addEventListener('submit', function(event)
                    {
                        if (form.checkValidity() === false)
                        {
                            form.classList.add('was-validated');
                            event.preventDefault();
                            event.stopPropagation();
                        }
                        else
                        {
                            event.preventDefault();
                            event.stopPropagation();

                            var formData = new FormData($('.needs-validation')[0]);

                            let inputSize = $('#size-select').val();
                            let inputName = $('#name').val();
                            let inputCity = $('#city').val();
                            let inputAddress = $('#address').val();
                            let inputMobileNum = $('#mobile-num').val();
                            let inputMail = $('#mail').val();

                            $.ajax({
                                type: 'POST',
                                url: constants.serviceUrl + '/orders/add',
                                headers: {
                                    'Content-Type': 'application/json',
                                    'Authorization': app.authorizationService.getCredentials()
                                },
                                data: JSON.stringify({
                                    "name": inputName,
                                    "number": inputMobileNum,
                                    "city": inputCity,
                                    "address": inputAddress,
                                    "email": inputMail,
                                    "size": inputSize,
                                    "done": false,
                                    "sweatshirt": data
                                })
                            }).done((data, request) =>
                            {
                                let waitingSize = parseInt($("#waiting-size").text());
                                $("#waiting-size").text(waitingSize+1);
                                $("#order-success-modal").modal();
                                $(document).bind('keydown', function(e)
                                {
                                    if (e.which == 27)
                                    {
                                        $("#order-success-modal").modal('hide');
                                    }
                                });
                            }).fail((err) =>
                            {
                                console.log(err);
                                alert(err.responseText);
                                document.getElementById("error_register").hidden = false;
                            });

                            form.classList.remove('was-validated');

                        }
                    }, false);
                });
            })();

            $('#one-star').click(function()
            {
                $.ajax({
                    type: 'POST',
                    url: constants.serviceUrl + '/sweatshirts/change-rating',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    data: JSON.stringify({
                        "id": data['id'],
                        "rating": 1
                    })
                }).done((result) =>
                {
                    data['timesRated']++;
                    $('#avg-rating-modal').html('<b>' + result + '/5</b> (' + data['timesRated'] + ' гласа)');

                }).fail((err) =>
                {
                    console.log(err);
                    if (err.status == 401 || err.status == 403) {
                        document.getElementById("error_login").hidden = false;
                    }
                });
            });

            $('#two-star').click(function()
            {
                $.ajax({
                    type: 'POST',
                    url: constants.serviceUrl + '/sweatshirts/change-rating',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    data: JSON.stringify({
                        "id": data['id'],
                        "rating": 2
                    })
                }).done((result) =>
                {
                    data['timesRated']++;
                    $('#avg-rating-modal').html('<b>' + result + '/5</b> (' + data['timesRated'] + ' гласа)');
                }).fail((err) =>
                {
                    console.log(err);
                    if (err.status == 401 || err.status == 403) {
                        document.getElementById("error_login").hidden = false;
                    }
                });
            });

            $('#three-star').click(function()
            {
                $.ajax({
                    type: 'POST',
                    url: constants.serviceUrl + '/sweatshirts/change-rating',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    data: JSON.stringify({
                        "id": data['id'],
                        "rating": 3
                    })
                }).done((result) =>
                {
                    data['timesRated']++;
                    $('#avg-rating-modal').html('<b>' + result + '/5</b> (' + data['timesRated'] + ' гласа)');
                }).fail((err) =>
                {
                    console.log(err);
                    if (err.status == 401 || err.status == 403)
                    {
                        document.getElementById("error_login").hidden = false;
                    }
                });
            });

            $('#four-star').click(function()
            {
                $.ajax({
                    type: 'POST',
                    url: constants.serviceUrl + '/sweatshirts/change-rating',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    data: JSON.stringify({
                        "id": data['id'],
                        "rating": 4
                    })
                }).done((result) =>
                {
                    data['timesRated']++;
                    $('#avg-rating-modal').html('<b>' + result + '/5</b> (' + data['timesRated'] + ' гласа)');
                }).fail((err) =>
                {
                    console.log(err);
                    if (err.status == 401 || err.status == 403) {
                        document.getElementById("error_login").hidden = false;
                    }
                });
            });

            $('#five-star').click(function()
            {
                $.ajax({
                    type: 'POST',
                    url: constants.serviceUrl + '/sweatshirts/change-rating',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    data: JSON.stringify({
                        "id": data['id'],
                        "rating": 5
                    })
                }).done((result) =>
                {
                    data['timesRated']++;
                    $('#avg-rating-modal').html('<b>' + result + '/5</b> (' + data['timesRated'] + ' гласа)');
                }).fail((err) =>
                {
                    console.log(err);
                    if (err.status == 401 || err.status == 403) {
                        document.getElementById("error_login").hidden = false;
                    }
                });
            });

            $('#add-to-cart-button').click(function()
            {
                let inputSize = $('#size-select').val();
                if(auth === "ROLE_USER" || auth === "MODERATOR" || auth === "ADMIN" || auth === "ROOT-ADMIN")
                {
                    $.ajax({
                        type: 'POST',
                        url: constants.serviceUrl + '/save-order',
                        headers: {
                            'Content-Type': 'application/json',
                            //  'Authorization': app.authorizationService.getCredentials()
                        },
                        data: JSON.stringify({
                            "sweatshirt": data,
                            "username": app.authorizationService.getUsername(),
                            "size": inputSize
                        })
                    }).done((data, request) =>
                    {
                        let cartSize = parseInt($("#cart-size").text());
                        $("#cart-size").text(cartSize+1);
                        $("#cart-success").modal();
                        $(document).bind('keydown', function(e)
                        {
                            if (e.which == 27)
                            {
                                $("#cart-success").modal('hide');
                            }
                        });
                    }).fail((err) =>
                    {
                        alert("Failed saving order. Please try again later");
                        console.log(err);
                        alert(err.responseText);
                        document.getElementById("error_register").hidden = false;
                    });
                }
                else
                {
                    $('#login-modal').modal();
                    $(document).bind('keydown', function(e)
                    {
                        if (e.which == 27)
                        {
                            $("#login-modal").modal('hide');
                        }
                    });

                    $('#sign-up-link').click(function ()
                    {
                        $('#login-modal').modal('hide');
                        // $('#order-modal').modal('hide');
                        setTimeout(function(){ app.router.reload('#/users/register'); }, 150);
                    });
                    $('#login-user').click(function ()
                    {
                        loginAndAddToCart(data, inputSize);
                    });
                }
            });

        });

        }).fail((err) =>
        {
        alert('Error');
        console.log(err.responseText);
    });
});

app.router.on('#/', null, function ()
{
    if(!loggedIn)
    {
        loadNavbar();
    }

    $.ajax({
        type: 'GET',
        url: constants.serviceUrl + '/',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': app.authorizationService.getCredentials()
        }
    }).done((data) => {
        app.templateLoader.loadTemplate('.app', 'sweatshirts-all', function ()
        {
            let auth = app.authorizationService.getRole();
            let i = 0;
            let k = 0;
            for (let elem of data)
            {
                let uniqueElementId = elem['id'];
                let currentAviability = elem['rAvailability'];
                let actionsId = 'actions-' + uniqueElementId;
                let uniqueElementIdFirst = uniqueElementId + '1';
                let uniqueElementIdSecond = uniqueElementId + '2';
                let uniqueElementIdThird = uniqueElementId + '3';
                let uniqueElementIdFourth = uniqueElementId + '4';
                let uniqueElementIdFifth = uniqueElementId + '5';
                let avg_rating = 'avg_rating_' + uniqueElementId;
                let uniqueElementIdFirstModal = uniqueElementId + 'Modal1';
                let uniqueElementIdSecondModal = uniqueElementId + 'Modal2';
                let uniqueElementIdThirdModal = uniqueElementId + 'Modal3';
                let uniqueElementIdFourthModal = uniqueElementId + 'Modal4';
                let uniqueElementIdFifthModal = uniqueElementId + 'Modal5';
                let name = uniqueElementId + 'Name';
                let selectedSize = uniqueElementId + 'Size';
                let mobileNum = uniqueElementId + 'MobileNum';
                let city = uniqueElementId + 'City';
                let address = uniqueElementId + 'Address';
                let mail = uniqueElementId + 'Mail';
                let submitButtonId = uniqueElementId + 'Submit';
                let deleteButtonId = uniqueElementId + 'Delete';
                let increaseProductAviability = uniqueElementId + 'Increase';
                let decreaseProductAviability = uniqueElementId + 'Descrease';
                let saveProductAviability = uniqueElementId + 'Save';
                let tempAviability = uniqueElementId + 'tempAviability';
                let avgRatingModal = 'avg_rating_modal' + uniqueElementId;
                let order = 'order' + uniqueElementId;
                let addToCart = 'addToCart' + uniqueElementId;
                let sizeSelect = 'sizeSelect' + uniqueElementId;
                let sizesId = 'sizes' + i;
                let saving = elem['oldPrice'] - elem['newPrice'];
                let fullSize = '';
                let options = '<option value="">Избери величина</option>';
                let selectedRating = 5;
                let avgRating;
                let ratingFlag = false;
                let loginUser = 'login' + uniqueElementId;


                if(i%3 == 0 || i==0)
                {
                    k++;
                    $('.card-container').append('<div id="' + k + '" class="row active-with-click">');
                }

                $('#' + k)
                    .append('<div class="kard col-md-4 col-sm-6 col-xs-12">'
                        +      '<article class="material-card Grey">'
                        +               '<h2>'
                        +                     '<span><a style="color:rgb(255,255,255);" href="#/sweatshirts/product?id=' + elem['id'] + '">' + elem['name'] + '</a>'
                        +                        '<button class="btn btn-outline-success btn-sm ml-2" id="' + increaseProductAviability + '" hidden="true"><i class="fas fa-plus"></i></button>'
                        +                        '<button class="btn btn-outline-danger btn-sm ml-1" type="click" id="' + decreaseProductAviability + '" hidden="true"><i class="fas fa-minus"></i></button>'
                        +                        '<button class="btn btn-outline-info btn-sm ml-1" type="click" id="' + saveProductAviability + '" hidden="true"><i class="fas fa-save"></i></button>'
                        +                     '</span>'
                        +                     '<strong>'
                        +                     '<div class="row pl-3">'
                        +                       '<span class="price old-price">'
                        +                              elem['oldPrice'] + 'ден. </span>'
                        +                       '<span class="price promo-price ml-2"> <b>'
                        +                              elem['newPrice'] + ' ден.</b></span>'
                        +                       '<button id="' + order + '" class="btn btn-danger ml-auto mr-1"><b>Порачај</b></button>'
                        +                     '</div>'
                        +                     '</strong>'
                        +                '</h2>'
                        +                '<div class="mc-content product-grid8">'
                        +                   '<div class="img-container product-image8">'
                        // +                       '<img class="img-responsive" src="' + elem['img'] + '">'
                        +                       '<img class="pic-1 img-responsive" src="' + elem['img1'] + '">'
                        +                       '<img class="pic-2 img-responsive" src="' + elem['img2'] + '">'
                        +                   '</div>'
                        +                   '<div class="mc-description">'
                        +                         elem['description']
                        +                   '</div>'
                        +                   '<div class="mc-leftovers">Останати: '
                        +                         '<b id="' + tempAviability + '">'
                        +                             elem['rAvailability']
                        +                         '</b>'
                        +                   '</div>'
                        +                   '<div id="' + sizesId + '" class="row mc-sizes justify-content-center align-items-center">'
                        +                   '</div>'
                        +                '</div>'
                        +                '<a class="mc-btn-action">'
                        +                    '<i class="fa fa-bars"></i>'
                        +                '</a>'
                        +                '<button type="click" id="' + deleteButtonId + '" hidden="true">x</button>'
                        +                '<div class="mc-footer">'
                        +                    '<div class="wrp">'
                        +                         '<a href="www.facebook.com" class="icon icon-facebook"><i class="fab fa-facebook-f"></i></a>'
                        +                         '<a href="www.instagram.com" class="icon icon-instagram mr-2"><i class="fab fa-instagram"></i></a>'
                        +                         '<a>'
                        +                           '<div class="rating" id="' + actionsId + '" style="margin-left: 83px;">'
                        +                               '<b><p class="ml-2" id="' + avg_rating + '"></p></b>'
                        +                               '<input type="radio" name="rating" value="5" id="' + uniqueElementIdFifth + '"><label for="' + uniqueElementIdFifth + '">☆</label>'
                        +                               '<input type="radio" name="rating" value="4" id="' + uniqueElementIdFourth + '"><label for="' + uniqueElementIdFourth + '">☆</label>'
                        +                               '<input type="radio" name="rating" value="3" id="' + uniqueElementIdThird + '"><label for="' + uniqueElementIdThird + '">☆</label>'
                        +                               '<input type="radio" name="rating" value="2" id="' + uniqueElementIdSecond + '"><label for="' + uniqueElementIdSecond + '">☆</label>'
                        +                               '<input type="radio" name="rating" value="1" id="' + uniqueElementIdFirst + '"><label for="' + uniqueElementIdFirst + '">☆</label>'
                        +                           '</div>'
                        +                         '</a>'
                        +                    '</div>'
                        +                '</div>'
                        +            '</article>'
                        +            '</div>');

                for(let size of elem['sizes'])
                {
                    fullSize += size;

                    if(size != 'X')
                    {
                        $('#' + sizesId).append('<div class="mr-3 p-2 bg-dark text-white" style="font-size: 25px !important;"><b>' + fullSize + '</b></div>');
                        options += '<option value="' + fullSize + '">' + fullSize + '</option>';
                        fullSize = '';
                    }
                }

                if((i+1)%3 == 0)
                {
                    $('.card-container').append('</div');
                }

                if(auth === 'ROOT-ADMIN' || auth === 'ADMIN')
                {
                    document.getElementById(increaseProductAviability).hidden = false;
                    document.getElementById(decreaseProductAviability).hidden = false;
                    document.getElementById(saveProductAviability).hidden = false;
                    document.getElementById(deleteButtonId).hidden = false;

                    $('#' + increaseProductAviability).click(function()
                    {
                        currentAviability++;
                        $('#' + tempAviability).text(currentAviability);
                    });

                    $('#' + decreaseProductAviability).click(function()
                    {
                        currentAviability--;
                        $('#' + tempAviability).text(currentAviability);
                    });

                    $('#' + saveProductAviability).click(function()
                    {
                        $.ajax({
                            type: 'POST',
                            url: constants.serviceUrl + '/sweatshirts/update-availability',
                            headers: {
                                'Content-Type': 'application/json',
                            },
                            data: JSON.stringify({
                                "id": elem['id'],
                                "availability": currentAviability
                            })
                        }).done((data) =>
                        {
                            console.log('Success');
                        }).fail((err) =>
                        {
                            alert('Fail');
                        });
                    });

                    $('#' + deleteButtonId).click(function()
                    {
                        var result = confirm("Do you really want to remove the sweatshirt?");
                        if(result)
                        {
                            $.ajax({
                                type: 'POST',
                                url: constants.serviceUrl + '/sweatshirts/remove?id=' + uniqueElementId,
                                headers: {
                                    'Content-Type': 'application/json',
                                    'Authorization': app.authorizationService.getCredentials()
                                }
                            }).done((data) => {
                                console.log(data);
                                app.router.reload('#/');
                            }).fail((err) => {
                                alert('Error while trying to delete the specified sweatshirt');
                                console.log(err);
                            });
                        }
                    });
                }

                $('#' + order).click(function()
                {
                    // $("#order-success-modal").modal();
                    $("#sweatshirt-name").html('<b>' + elem['name'] + '</b>');
                    $("#modal-img").attr("src", elem['img1']);
                    $("#rating-system").html
                    (           '<p class="mt-2 ml-2" id="' + avgRatingModal + '"></p>'
                        +       '<input type="radio" name="rating" value="5" id="' + uniqueElementIdFifthModal + '"><label for="' + uniqueElementIdFifthModal + '">☆</label>'
                        +       '<input type="radio" name="rating" value="4" id="' + uniqueElementIdFourthModal + '"><label for="' + uniqueElementIdFourthModal + '">☆</label>'
                        +       '<input type="radio" name="rating" value="3" id="' + uniqueElementIdThirdModal + '"><label for="' + uniqueElementIdThirdModal + '">☆</label>'
                        +       '<input type="radio" name="rating" value="2" id="' + uniqueElementIdSecondModal + '"><label for="' + uniqueElementIdSecondModal + '">☆</label>'
                        +       '<input type="radio" name="rating" value="1" id="' + uniqueElementIdFirstModal + '"><label for="' + uniqueElementIdFirstModal + '">☆</label>'
                    );
                    $("#order-col").html
                    (
                        '<form class="needs-validation" novalidate>'
                        +       '<div class="input-group form-group" id="">'
                        +           '<select class="form-control" id="' + sizeSelect + '" required>'
                        +           '</select>'
                        +           '<div class="invalid-feedback">'
                        +               'Избери величина'
                        +           '</div>'
                        +       '</div>'
                        +       '<div class="input-group form-group">'
                        +           '<div class="input-group-prepend">'
                        +               '<span class="input-group-text"><i class="fas fa-user-tie"></i></span>'
                        +           '</div>'
                        +           '<input id="' + name + '" type="text" class="form-control rounded-right" placeholder="Име и презиме" minlength="5" maxlength="100" required>'
                        +           '<div class="invalid-feedback">'
                        +               'Името и презимето треба да е над 5 букви'
                        +           '</div>'
                        +       '</div>'
                        +       '<div class="input-group form-group">'
                        +           '<div class="input-group-prepend">'
                        +               '<span class="input-group-text"><i class="fas fa-phone-alt"></i></span>'
                        +           '</div>'
                        +           '<input id="' + mobileNum + '" type="tel" class="form-control rounded-right" placeholder="Мобилен број" minlength="3" maxlength="100" required>'
                        +           '<div class="invalid-feedback">'
                        +               'Мобилниот број треба да е на 5 бројки'
                        +           '</div>'
                        +       '</div>'
                        +       '<div class="input-group form-group">'
                        +           '<div class="input-group-prepend">'
                        +               '<span class="input-group-text"><i class="fas fa-city"></i></span>'
                        +           '</div>'
                        +           '<input id="' + city + '" type="text" class="form-control rounded-right" placeholder="Град" minlength="3" maxlength="100" required>'
                        +           '<div class="invalid-feedback">'
                        +               'Името на град треба да е над 3 букви'
                        +           '</div>'
                        +       '</div>'
                        +       '<div class="input-group form-group">'
                        +           '<div class="input-group-prepend">'
                        +               '<span class="input-group-text"><i class="fas fa-map-marked-alt"></i></span>'
                        +          '</div>'
                        +           '<input id="' + address + '" type="text" class="form-control rounded-right" placeholder="Адреса" minlength="3" maxlength="100" required>'
                        +           '<div class="invalid-feedback">'
                        +              'Адресата треба да е над 3 букви'
                        +           '</div>'
                        +       '</div>'
                        +       '<div class="input-group form-group">'
                        +           '<div class="input-group-prepend">'
                        +               '<span class="input-group-text"><i class="fas fa-envelope"></i></span>'
                        +           '</div>'
                        +           '<input id="' + mail + '" type="email" class="form-control rounded-right" placeholder="Е-маил" maxlength="100" required>'
                        +           '<div class="invalid-feedback">'
                        +               'Воведете валиден меил'
                        +           '</div>'
                        +       '</div>'
                        +       '<button type="click" id="' + submitButtonId + '" class="btn btn-success btn-block">Порачај</button>'
                        +       '</form>'
                    );

                    $("#order-modal").modal('show');
                    $("#add-to-cart").html('<button  id="' + addToCart + '" class="btn btn-dark btn-block">Додади во количката</button>');
                    $("#old-price").text(elem['oldPrice'] + 'ден.');
                    $("#new-price").html('<b>' + elem['newPrice'] + 'ден.</b>');
                    $("#saving").text('Вие заштедувате ' + saving + ' денари');
                    $("#lead").text(elem['description']);
                    $('#' + sizeSelect).html(options);

                    (function() {
                        'use strict';
                        var forms = document.getElementsByClassName('needs-validation');
                        var validation = Array.prototype.filter.call(forms, function(form) {
                            form.addEventListener('submit', function(event)
                            {
                                if (form.checkValidity() === false)
                                {
                                    form.classList.add('was-validated');
                                    event.preventDefault();
                                    event.stopPropagation();
                                }
                                else
                                {
                                    event.preventDefault();
                                    event.stopPropagation();

                                    var formData = new FormData($('.needs-validation')[0]);

                                    let inputSize = $('#' + sizeSelect).val();
                                    let inputName = $('#' + name).val();
                                    let inputCity = $('#' + city).val();
                                    let inputAddress = $('#' + address).val();
                                    let inputMobileNum = $('#' + mobileNum).val();
                                    let inputMail = $('#' + mail).val();

                                    $.ajax({
                                        type: 'POST',
                                        url: constants.serviceUrl + '/orders/add',
                                        headers: {
                                            'Content-Type': 'application/json',
                                            'Authorization': app.authorizationService.getCredentials()
                                        },
                                        data: JSON.stringify({
                                            "name": inputName,
                                            "number": inputMobileNum,
                                            "city": inputCity,
                                            "address": inputAddress,
                                            "email": inputMail,
                                            "size": inputSize,
                                            "done": false,
                                            "sweatshirt": elem
                                        })
                                    }).done((data, request) =>
                                    {
                                        let waitingSize = parseInt($("#waiting-size").text());
                                        $("#waiting-size").text(waitingSize+1);
                                        $("#order-success-modal").modal();
                                        $(document).bind('keydown', function(e)
                                        {
                                            if (e.which == 27)
                                            {
                                                $("#order-success-modal").modal('hide');
                                            }
                                        });
                                    }).fail((err) =>
                                    {
                                        console.log(err);
                                        alert(err.responseText);
                                        document.getElementById("error_register").hidden = false;
                                    });

                                    form.classList.remove('was-validated');

                                }
                            }, false);
                        });
                    })();

                    $('#' + uniqueElementIdFirstModal).click(function()
                    {
                        $.ajax({
                            type: 'POST',
                            url: constants.serviceUrl + '/sweatshirts/change-rating',
                            headers: {
                                'Content-Type': 'application/json',
                            },
                            data: JSON.stringify({
                                "id": uniqueElementId,
                                "rating": 1
                            })
                        }).done((data) =>
                        {
                            elem['timesRated']++;
                            $('#' + avgRatingModal).html('<b>' + data + '/5</b> (' + elem['timesRated'] + ' гласа)');

                        }).fail((err) =>
                        {
                            console.log(err);
                            if (err.status == 401 || err.status == 403) {
                                document.getElementById("error_login").hidden = false;
                            }
                        });
                    });

                    $('#' + uniqueElementIdSecondModal).click(function()
                    {
                        $.ajax({
                            type: 'POST',
                            url: constants.serviceUrl + '/sweatshirts/change-rating',
                            headers: {
                                'Content-Type': 'application/json',
                            },
                            data: JSON.stringify({
                                "id": uniqueElementId,
                                "rating": 2
                            })
                        }).done((data) =>
                        {
                            elem['timesRated']++;
                            $('#' + avgRatingModal).html('<b>' + data + '/5</b> (' + elem['timesRated'] + ' гласа)');
                        }).fail((err) =>
                        {
                            console.log(err);
                            if (err.status == 401 || err.status == 403) {
                                document.getElementById("error_login").hidden = false;
                            }
                        });
                    });

                    $('#' + uniqueElementIdThirdModal).click(function()
                    {
                        $.ajax({
                            type: 'POST',
                            url: constants.serviceUrl + '/sweatshirts/change-rating',
                            headers: {
                                'Content-Type': 'application/json',
                            },
                            data: JSON.stringify({
                                "id": uniqueElementId,
                                "rating": 3
                            })
                        }).done((data) =>
                        {
                            elem['timesRated']++;
                            $('#' + avgRatingModal).html('<b>' + data + '/5</b> (' + elem['timesRated'] + ' гласа)');
                        }).fail((err) =>
                        {
                            console.log(err);
                            if (err.status == 401 || err.status == 403)
                            {
                                document.getElementById("error_login").hidden = false;
                            }
                        });
                    });

                    $('#' + uniqueElementIdFourthModal).click(function()
                    {
                        $.ajax({
                            type: 'POST',
                            url: constants.serviceUrl + '/sweatshirts/change-rating',
                            headers: {
                                'Content-Type': 'application/json',
                            },
                            data: JSON.stringify({
                                "id": uniqueElementId,
                                "rating": 4
                            })
                        }).done((data) =>
                        {
                            elem['timesRated']++;
                            $('#' + avgRatingModal).html('<b>' + data + '/5</b> (' + elem['timesRated'] + ' гласа)');
                        }).fail((err) =>
                        {
                            console.log(err);
                            if (err.status == 401 || err.status == 403) {
                                document.getElementById("error_login").hidden = false;
                            }
                        });
                    });

                    $('#' + uniqueElementIdFifthModal).click(function()
                    {
                        $.ajax({
                            type: 'POST',
                            url: constants.serviceUrl + '/sweatshirts/change-rating',
                            headers: {
                                'Content-Type': 'application/json',
                            },
                            data: JSON.stringify({
                                "id": uniqueElementId,
                                "rating": 5
                            })
                        }).done((data) =>
                        {
                            elem['timesRated']++;
                            $('#' + avgRatingModal).html('<b>' + data + '/5</b> (' + elem['timesRated'] + ' гласа)');
                        }).fail((err) =>
                        {
                            console.log(err);
                            if (err.status == 401 || err.status == 403) {
                                document.getElementById("error_login").hidden = false;
                            }
                        });
                    });

                    $('#' + addToCart).click(function()
                    {
                        document.getElementById("error_login").hidden = true;
                        auth = app.authorizationService.getRole();
                        let inputSize = $('#' + sizeSelect).val();
                        if(auth === "ROLE_USER" || auth === "MODERATOR" || auth === "ADMIN" || auth === "ROOT-ADMIN")
                        {
                            $.ajax({
                                type: 'POST',
                                url: constants.serviceUrl + '/save-order',
                                headers: {
                                    'Content-Type': 'application/json',
                                  //  'Authorization': app.authorizationService.getCredentials()
                                },
                                data: JSON.stringify({
                                    "sweatshirt": elem,
                                    "username": app.authorizationService.getUsername(),
                                    "size": inputSize
                                })
                            }).done((data, request) =>
                            {
                                let cartSize = parseInt($("#cart-size").text());
                                $("#cart-size").text(cartSize+1);
                                $("#cart-success").modal();
                                $(document).bind('keydown', function(e)
                                {
                                    if (e.which == 27)
                                    {
                                        $("#cart-success").modal('hide');
                                    }
                                });
                            }).fail((err) =>
                            {
                                console.log(err);
                                alert(err.responseText);
                                document.getElementById("error_register").hidden = false;
                            });
                        }
                        else
                        {
                            $('#login-modal').modal();
                            $("#button-div").html('<button id="' + loginUser + '"'
                                + ' type="button" class="btn float-right login_btn">Login</button>');
                            $('#sign-up-link').click(function ()
                            {
                                $('#login-modal').modal('hide');
                                $('#order-modal').modal('hide');
                                setTimeout(function(){ app.router.reload('#/users/register'); }, 150);
                            });
                            $('#' + loginUser).click(function ()
                            {
                                loginAndAddToCart(elem, inputSize);
                            });
                        }
                    });

                    $(document).bind('keydown', function(e)
                    {
                        if (e.which == 27)
                        {
                            var isCartModalOn = $('#cart-success').is(':visible');
                            var isOrderSuccessModalOn = $('#order-success-modal').is(':visible');
                            var isLoginModalOn = $('#login-modal').is(':visible');
                            if(isCartModalOn == false && isOrderSuccessModalOn == false && isLoginModalOn == false)
                            {
                                $("#order-modal").modal('hide');
                            }
                        }
                    });
                });

                $('#' + uniqueElementIdFirst).click(function()
                {
                    $.ajax({
                        type: 'POST',
                        url: constants.serviceUrl + '/sweatshirts/change-rating',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        data: JSON.stringify({
                            "id": uniqueElementId,
                            "rating": 1
                        })
                    }).done((data) =>
                    {
                        elem['timesRated']++;
                        $('#' + avg_rating).text(data);
                    }).fail((err) =>
                    {
                        console.log(err);
                        if (err.status == 401 || err.status == 403) {
                            document.getElementById("error_login").hidden = false;
                        }
                    });
                });


                $('#' + uniqueElementIdSecond).click(function()
                {
                    $.ajax({
                        type: 'POST',
                        url: constants.serviceUrl + '/sweatshirts/change-rating',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        data: JSON.stringify({
                            "id": uniqueElementId,
                            "rating": 2
                        })
                    }).done((data) => {
                        elem['timesRated']++;
                        $('#' + avg_rating).text(data);
                    }).fail((err) => {
                        console.log(err);
                        if (err.status == 401 || err.status == 403) {
                            document.getElementById("error_login").hidden = false;
                        }
                    });
                });

                $('#' + uniqueElementIdThird).click(function()
                {
                    $.ajax({
                        type: 'POST',
                        url: constants.serviceUrl + '/sweatshirts/change-rating',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        data: JSON.stringify({
                            "id": uniqueElementId,
                            "rating": 3
                        })
                    }).done((data) => {
                        elem['timesRated']++;
                        $('#' + avg_rating).text(data);
                    }).fail((err) => {
                        console.log(err);
                        if (err.status == 401 || err.status == 403) {
                            document.getElementById("error_login").hidden = false;
                        }
                    });
                });

                $('#' + uniqueElementIdFourth).click(function()
                {
                    $.ajax({
                        type: 'POST',
                        url: constants.serviceUrl + '/sweatshirts/change-rating',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        data: JSON.stringify({
                            "id": uniqueElementId,
                            "rating": 4
                        })
                    }).done((data) => {
                        elem['timesRated']++;
                        $('#' + avg_rating).text(data);
                    }).fail((err) => {
                        console.log(err);
                        if (err.status == 401 || err.status == 403) {
                            document.getElementById("error_login").hidden = false;
                        }
                    });
                });

                $('#' + uniqueElementIdFifth).click(function()
                {
                    $.ajax({
                        type: 'POST',
                        url: constants.serviceUrl + '/sweatshirts/change-rating',
                        headers: {
                            'Content-Type': 'application/json',
                        },
                        data: JSON.stringify({
                            "id": uniqueElementId,
                            "rating": 5
                        })
                    }).done((data) => {
                        elem['timesRated']++;
                        $('#' + avg_rating).text(data);
                    }).fail((err) => {
                        console.log(err);
                        if (err.status == 401 || err.status == 403) {
                            document.getElementById("error_login").hidden = false;
                        }
                    });
                });

                i++;
            }

            $('.card-container').append('</div');

            if(auth === "MODERATOR" || auth === "ADMIN" || auth === "ROOT-ADMIN")
            {
                (function () {
                    'use strict';
                    var forms = document.getElementsByClassName('needs-validation2');
                    var validation = Array.prototype.filter.call(forms, function (form) {
                        form.addEventListener('submit', function (event) {
                            if (form.checkValidity() === false)
                            {
                                form.classList.add('was-validated');
                                event.preventDefault();
                                event.stopPropagation();
                            }
                            else
                            {
                                event.preventDefault();
                                event.stopPropagation();

                                var formData = new FormData($('.needs-validation2')[0]);

                                let name = $('#name').val();
                                let img = $('#img').val();
                                let newPrice = $('#new_price_input').val();
                                let oldPrice = $('#old_price_input').val();
                                let sizes = $('#sizes').val();
                                let availability = $('#availability_input').val();
                                let rAvailability = $('#r_availability_input').val();
                                let description = $('#description').val();

                                $.ajax({
                                    type: 'POST',
                                    url: constants.serviceUrl + '/sweatshirts/add',
                                    headers: {
                                        'Content-Type': 'application/json',
                                    },
                                    data: JSON.stringify({
                                        "name": name,
                                        "img": img,
                                        "newPrice": newPrice,
                                        "oldPrice": oldPrice,
                                        "sizes": sizes,
                                        "аvailability": availability,
                                        "rAvailability": rAvailability,
                                        "description": description
                                    })
                                }).done((data) => {
                                    app.router.reload('#/');
                                    setTimeout(function () {
                                        $('#add_success').modal();
                                    }, 1000);
                                    $(document).bind('keydown', function(e)
                                    {
                                        if (e.which == 27)
                                        {
                                            $("#add_success").modal('hide');
                                        }
                                    });
                                }).fail((err) => {
                                    alert('err');
                                });

                                form.classList.remove('was-validated');
                            }
                        }, false);
                    });
                })();
            }
            else
            {
                $("#add-sweatshirt").hide();
            }
        });
    }).fail((err) => {
        console.log(err);
    });
});

app.router.on("#/users/all", null, function () {

    $.ajax({
        type: 'GET',
        url: constants.serviceUrl + '/users/all',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': app.authorizationService.getCredentials()
        }
    }).done((data) =>
    {
        app.templateLoader.loadTemplate('.app', 'users-all', function () {
            let loggedUsername = app.authorizationService.getUsername();
            $('#all-users').text('Welcome, ' + loggedUsername);

            let i = 1;
            for (let elem of data) {

                var formatDateBirth = new Date(elem['birthDate']).toLocaleDateString();

                if(elem['role'] === 'ROOT-ADMIN') {
                    $('.all-users')
                        .append('<tr>'
                            // + '<td class="id" style="display:none;">' + elem['id'] + '</td>'
                            + '<td class="first-name">' + elem['firstName'] + '</td>'
                            + '<td class="last-name">' + elem['lastName'] + '</td>'
                            + '<td class="username">' + elem['username'] + '</td>'
                            + '<td class="email">' + elem['email'] + '</td>'
                            + '<td class="date-birth">' + formatDateBirth + '</td>'
                            + '<td class="text-center">'
                            + '<div class="d-flex justify-content-center">'
                            + '<button type="button" class="edit-btn btn" disabled>'
                            + '<i class="fa fa-edit"></i>'
                            + '</button>'
                            + '<button type="button" class="delete-btn btn" disabled>'
                            + '<i class="fas fa-trash-alt"></i>'
                            + '</button>'
                            + '</div>'
                            + '</td>'
                            + '</tr>');
                }
                else {
                    $('.all-users')
                        .append('<tr>'
                            // + '<td class="id" style="display:none;">' + elem['id'] + '</td>'
                            + '<td class="first-name">' + elem['firstName'] + '</td>'
                            + '<td class="last-name">' + elem['lastName'] + '</td>'
                            + '<td class="username">' + elem['username'] + '</td>'
                            + '<td class="email">' + elem['email'] + '</td>'
                            + '<td class="date-birth">' + formatDateBirth + '</td>'
                            + '<td class="text-center">'
                            + '<div class="d-flex justify-content-center">'
                            + '<button type="button" class="edit-btn btn">'
                            + '<i class="fa fa-edit"></i>'
                            + '</button>'
                            + '<button type="button" class="delete-btn btn">'
                            + '<i class="fas fa-trash-alt"></i>'
                            + '</button>'
                            + '</div>'
                            + '</td>'
                            + '</tr>');
                }

                i++;
            }

            if(app.authorizationService.getRole() === 'ROLE_USER') {
                $( ".edit-btn" ).prop( "disabled", true );
                $( ".delete-btn" ).prop( "disabled", true );
            }
            else if(app.authorizationService.getRole() === 'MODERATOR') {
                $( ".delete-btn" ).prop( "disabled", true );
            }

            $('#refresh-users-button').click(function() {
                app.router.reload('#/users/all');
            });

            $('#create_account').click( function ()
            {
                $("#create_modal").modal();
            });

            $('.edit-btn').click( function ()
            {
                var $row = $(this).closest("tr");
                var $firstName = $row.find(".first-name").text();
                var $lastName = $row.find(".last-name").text();
                var $username = $row.find(".username").text();
                var $email = $row.find(".email").text();
                var $dateBirth = $row.find(".date-birth").text();

                $modal = $('#edit_modal');
                $editor = $('#editor');

                $editor.find('#firstName').val($firstName);
                $editor.find('#lastName').val($lastName);
                $editor.find('#username_edit').val($username);
                $editor.find('#email').val($email);
                $editor.find('#datetimepicker_modal').val($dateBirth);
                $modal.data('row', $row);
                $modal.modal('show');
            });

            $('.delete-btn').click( function ()
            {
                var result = confirm("Do you really want to remove the user?");
                if (result) {

                    var $row = $(this).closest("tr");
                    let username = $row.find(".username").text();

                    $.ajax({
                        type: 'POST',
                        url: constants.serviceUrl + '/users/delete?username=' + username,
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': app.authorizationService.getCredentials()
                        }
                    }).done(() => {
                        app.router.reload('#/users/all');
                    }).fail((err) => {
                        console.log(err);
                        // alert(err.responseText);
                    }).always(function () {
                        //    app.router.reload('#/users/all');
                    });
                }
            });


            (function() {
                'use strict';
                var forms = document.getElementsByClassName('needs-validation');
                var validation = Array.prototype.filter.call(forms, function(form) {
                    form.addEventListener('submit', function(event) {
                        if (form.checkValidity() === false) {
                            form.classList.add('was-validated');
                            event.preventDefault();
                            event.stopPropagation();
                        }
                        else
                        {
                            event.preventDefault();
                            event.stopPropagation();

                            var formData = new FormData($('.needs-validation')[0]);

                            let firstName = $('#firstName').val();
                            let lastName = $('#lastName').val();
                            let username = $('#username_edit').val();
                            let email = $('#email').val();
                            let birthDate = $('#datetimepicker_modal').val();
                            let getBirthDate = new Date(birthDate);

                            $.ajax({
                                type: 'POST',
                                url: constants.serviceUrl + '/users/edit',
                                headers: {
                                    'Content-Type': 'application/json',
                                    'Authorization': app.authorizationService.getCredentials()
                                },
                                data: JSON.stringify({
                                    "firstName": firstName,
                                    "lastName": lastName,
                                    "username": username,
                                    "email": email,
                                    "birthDate": getBirthDate,
                                })
                            }).done(() => {
                                $('#edit_modal').modal('hide');
                                setTimeout(function(){ app.router.reload('#/users/all'); }, 150);
                            }).fail((err) => {
                                console.log(err);
                                alert(err.responseText);
                            }).always(function () {
                                //    app.router.reload('#/users/all');
                            });
                            form.classList.remove('was-validated');
                        }
                    }, false);
                });
            })();
            // Create Modal
            (function() {
                'use strict';
                var forms = document.getElementsByClassName('needs-validation-create');
                var validation = Array.prototype.filter.call(forms, function(form) {
                    form.addEventListener('submit', function(event) {
                        if (form.checkValidity() === false) {
                            form.classList.add('was-validated');
                            event.preventDefault();
                            event.stopPropagation();
                        }
                        else
                        {
                            event.preventDefault();
                            event.stopPropagation();

                            var formData = new FormData($('.needs-validation')[0]);

                            let firstName = $('#first-name-create').val();
                            let lastName = $('#last-name-create').val();
                            let username = $('#username-create').val();
                            let email = $('#email-create').val();
                            let birthDate = $('#datetimepicker_modal_create').val();
                            let password = $('#password-create').val();
                            let confirmPassword = $('#confirm-password-create').val();

                            let formatBirthDate = new Date(birthDate);

                            $.ajax({
                                type: 'POST',
                                url: constants.serviceUrl + '/users/register',
                                headers: {
                                    'Content-Type': 'application/json',
                                    'Authorization': app.authorizationService.getCredentials()
                                },
                                data: JSON.stringify({
                                    "firstName": firstName,
                                    "lastName": lastName,
                                    "username": username,
                                    "email": email,
                                    "birthDate": formatBirthDate,
                                    "password": password,
                                    "confirmPassword": confirmPassword,
                                })
                            }).done(() => {
                                $('#create_modal').modal('hide');
                                setTimeout(function(){ app.router.reload('#/users/all'); }, 150);
                            }).fail((err) => {
                                console.log(err);
                                alert(err.responseText);
                            }).always(function () {
                                //    app.router.reload('#/users/all');
                            });
                            form.classList.remove('was-validated');
                        }
                    }, false);
                });
            })();

        });
    }).fail((err) => {
        console.log(err);
    });
});

app.router.on("#/organization", null, function () {
    $.ajax({
        type: 'GET',
        url: constants.serviceUrl + '/organization',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': app.authorizationService.getCredentials()
        }
    }).done((data) => {
        app.templateLoader.loadTemplate('.app', 'organization', function () {
            let i = 1;

            for (let elem of data)
            {
                let uniqueElementId = elem['id'];
                let actionsId = 'actions-' + uniqueElementId;

                $('.all-users-organization')
                    .append('<tr>'
                        + '<td class="id">' + i + '</td>'
                        + '<td class="first-name">' + elem['firstName'] + '</td>'
                        + '<td class="last-name">' + elem['lastName'] + '</td>'
                        + '<td class="username">' + elem['username'] + '</td>'
                        + '<td class="role">' + elem['role'] + '</td>'
                        + '<td id="' + actionsId + '" class="col-md-7 d-flex justify-content-between" scope="col">'
                        + '</td>'
                        + '</tr>');
                if(elem['username'] === app.authorizationService.getUsername()) {
                    $('#' + actionsId)
                        .append('<h5><button class="btn btn-secondary btn-sm px-4" disabled>Own Unchangeable</button></h5>');
                    uniqueElementId = null;
                }
                else {
                    if (elem['role'] === 'ROLE_USER') {
                        $('#' + actionsId)
                            .append('<h5><button class="btn btn-primary btn-sm promote-button">Promote</button></h5>')
                    } else if (elem['role'] === 'ROOT-ADMIN') {
                        $('#' + actionsId)
                            .append('<h5><button class="btn btn-secondary btn-sm px-4" disabled>Owner Unchangeable</button></h5>');
                        uniqueElementId = null;
                    } else if (elem['role'] === 'ADMIN') {
                        $('#' + actionsId)
                            .append('<h5><button class="btn btn-danger btn-sm demote-button">Demote</button></h5>');
                    }else {
                        $('#' + actionsId)
                            .append('<h5><button class="btn btn-primary btn-sm promote-button mr-2">Promote</button></h5>')
                            .append('<h5><button class="btn btn-danger btn-sm demote-button">Demote</button></h5>');
                    }
                }

                $('#' + actionsId + '>h5>.promote-button').click(function (e) {
                    $.ajax({
                        type: 'POST',
                        url: constants.serviceUrl + '/organization/promote?id=' + uniqueElementId,
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': app.authorizationService.getCredentials()
                        }
                    }).done(function (data) {
                        console.log(data);
                    }).fail(function (err) {
                        console.log(err);
                    }).always(function () {
                        app.router.reload('#/organization');
                    })
                });

                $('#' + actionsId + '>h5>.demote-button').click(function (e) {
                    $.ajax({
                        type: 'POST',
                        url: constants.serviceUrl + '/organization/demote?id=' + uniqueElementId,
                        headers: {
                            'Content-Type': 'application/json',
                            'Authorization': app.authorizationService.getCredentials()
                        }
                    }).done(function (data) {
                        console.log(data);
                    }).fail(function (err) {
                        console.log(err);
                    }).always(function () {
                        app.router.reload('#/organization');
                    });
                });

                $('#refresh-users-button').click(function() {
                    app.router.reload('#/organization');
                });

                i++;
            }
        });
    }).fail((err) => {
        console.log(err);
    });
});

app.router.on("#/logs/all", null, function () {

    $.ajax({
        type: 'GET',
        url: constants.serviceUrl + '/logs/all',
        headers: {
            'Authorization': app.authorizationService.getCredentials()
        }
    }).done((data) => {
        app.templateLoader.loadTemplate('.app', 'logs-all', function () {
            let i = 1;
            for (let elem of data)
            {
                $('.all-logs')
                    .append('<tr>'

                        + '<td class="first-name" style="display:none;">' + elem['id'] + '</td>'
                        + '<td class="user">' + elem['user'] + '</td>'
                        + '<td class="operation">' + elem['operation'] + '</td>'
                        + '<td class="table-name">' + elem['tableName'] + '</td>'
                        + '<td class="date">' + elem['date'] + '</td>'
                        + '</tr>');

                i++;
            }

            if(app.authorizationService.getRole() === 'MODERATOR') {
                $( "#clear_selected" ).prop( "disabled", true );
                $( "#clear_all" ).prop( "disabled", true )
            }

            var table = $('#example').DataTable();
            var selectedRows = new Array();

            $('#example tbody').on( 'click', 'tr', function ()
            {
                $(this).toggleClass('selected');
            } );

            $('#clear_selected').click( function ()
            {
                var rows = table.rows('.selected').data();

                for(var i=0;i<rows.length;i++)
                {
                    selectedRows[i] = rows[i][0];
                }

                $.ajax({
                    type: 'POST',
                    url: constants.serviceUrl + '/logs/delete?logs=' + selectedRows,
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': app.authorizationService.getCredentials()
                    }
                }).done(() => {
                    app.router.reload('#/logs/all');
                }).fail((err) => {
                    console.log(err);
                    alert('Err: ' + err);
                }).always(function () {
                });
            });

            $('#clear_all').click( function ()
            {
                $.ajax({
                    type: 'POST',
                    url: constants.serviceUrl + '/logs/delete/all',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': app.authorizationService.getCredentials()
                    }
                }).done(() => {
                    app.router.reload('#/`logs/all`');
                }).fail((err) => {
                    console.log(err);
                    alert(err);
                }).always(function () {
                });
            });
        });
    }).fail((err) => {
        console.log(err);
    });

    $('#refresh-logs-button').click(function() {
        app.router.reload('#/logs/all');
    });

});

app.router.on("#/orders/waiting", null, function ()
{
    // loadNavbar();
    $.ajax({
        type: 'GET',
        url: constants.serviceUrl + '/orders/waiting',
        headers:
       {
            'Content-Type': 'application/json',
            'Authorization': app.authorizationService.getCredentials()
        }
    }).done((data) =>
    {
        app.templateLoader.loadTemplate('.app', 'orders-waiting', function ()
        {
         //   let loggedUsername = app.authorizationService.getUsername();
         //   $('#logged-user').text('Welcome, ' + loggedUsername);

            let i = 1;

            for (let elem of data)
            {
                let uniqueElementId = elem['id'];

                if(elem['sweatshirtName'] === null)
                {
                    elem['sweatshirtId'] = 'Deleted';
                    elem['sweatshirtName'] = 'Deleted';
                }


                $('.all-waiting-orders')
                    .append('<tr>'
                        + '<td class="order-id" style="display:none;">' + elem['id'] + '</td>'
                        + '<td class="sweatshirt-id">' + elem['sweatshirtId'] + '</td>'
                        + '<td class="sweatshirt-name">' + elem['sweatshirtName'] + '</td>'
                        + '<td class="size">' + elem['size'] + '</td>'
                        + '<td class="name">' + elem['name'] + '</td>'
                        + '<td class="address">' + elem['address'] + '</td>'
                        + '<td class="email">' + elem['email'] + '</td>'
                        + '<td class="city">' + elem['city'] + '</td>'
                        + '<td class="number">' + elem['number'] + '</td>'
                        + '<td class="text-center">'
                        + '<div class="d-flex justify-content-center">'
                        + '<input id="' + uniqueElementId + '"type="checkbox" name="deliver" value="deliver">'
                        + '</div>'
                        + '</td>'
                        + '</tr>');

                $('#' + uniqueElementId).click(function (e)
                {
                    $('#approve-deliver-modal').modal();

                    $('#approve-deliver-button').click(function (e)
                    {
                        $.ajax({
                            type: 'POST',
                            url: constants.serviceUrl + '/orders/deliver?id=' + uniqueElementId,
                            headers: {
                                'Content-Type': 'application/json'
                        //      'Authorization': app.authorizationService.getCredentials()
                            }
                        }).done(function (data)
                        {
                            let deliveredSize = parseInt($("#delivered-size").text());
                            $("#delivered-size").text(deliveredSize+1);
                            let waitingSize = parseInt($("#waiting-size").text());
                            $("#waiting-size").text(waitingSize-1);
                            setTimeout(function()
                            {
                                app.router.reload('#/orders/waiting');
                            }, 150);

                        }).fail(function (err)
                        {
                            alert("FAIL");
                            console.log(err);
                        }).always(function ()
                        {
                            $('#approve-deliver-modal').modal('hide');
                        });
                    });

                    $('#approve-deliver-modal').on('hidden.bs.modal', function ()
                    {
                        $('#' + uniqueElementId).prop('checked', false);
                    })
                });

                i++;
            }

            var table = $('#orders-table').DataTable();
            var selectedRows = new Array();

            $('#orders-table tbody').on( 'click', 'tr', function ()
            {
                $(this).toggleClass('selected');
            } );

            $('#pdf-selected').click( function ()
            {
                let rows = table.rows('.selected').data();

                for(let i=0;i<rows.length;i++)
                {
                    $('.temp-table-data')
                        .append('<tr>'
                            + '<td>' + rows[i][0] + '</td>'
                            + '<td>' + rows[i][1] + '</td>'
                            + '<td>' + rows[i][2]  + '</td>'
                            + '<td>' + rows[i][3]  + '</td>'
                            + '<td>' + rows[i][4]  + '</td>'
                            + '<td>' + rows[i][5]  + '</td>'
                            + '<td>' + rows[i][6]  + '</td>'
                            + '<td>' + rows[i][7]  + '</td>'
                            + '<td>' + rows[i][8]  + '</td>'
                            + '</tr>');
                }

                var doc =new jsPDF();
                // doc.text("Title", 10, 10)
                doc.autoTable({ html: '#temp-table' });
                doc.save('Selected-Waiting-Orders.pdf');
                doc.languages
                $('#temp-table tbody').empty();
            });

            $('#pdf-all').click( function ()
            {
                var doc =new jsPDF();
                doc.autoTable({ html: '#orders-table' });
                doc.save('All-Waiting-Orders.pdf');
            });

            $('#delete-selected').click( function ()
            {
                let rows = table.rows('.selected').data();
                var selectedRows = new Array();

                for(let i=0;i<rows.length;i++)
                {
                    selectedRows[i] = rows[i][0];
                }

                $.ajax({
                    type: 'POST',
                    url: constants.serviceUrl + '/orders/remove-selected?selectedOrders=' + selectedRows,
                    headers:
                        {
                            'Content-Type': 'application/json',
                            'Authorization': app.authorizationService.getCredentials()
                        }
                }).done(() =>
                {
                    let waitingSize = parseInt($("#waiting-size").text());
                    $("#waiting-size").text(waitingSize-selectedRows.length);
                    alert('Delete success');
                    app.router.reload('#/orders/waiting');
                }).fail((err) =>
                {
                    console.log(err);
                    alert("Error: " + err);
                })
            });
        });
    }).fail((err) => {
        console.log(err);
    });
});

app.router.on("#/orders/delivered", null, function ()
{
    // loadNavbar();

    $.ajax({
        type: 'GET',
        url: constants.serviceUrl + '/orders/delivered',
        headers:
            {
                'Content-Type': 'application/json',
                'Authorization': app.authorizationService.getCredentials()
            }
    }).done((data) =>
    {
        app.templateLoader.loadTemplate('.app', 'orders-waiting', function ()
        {
            //   let loggedUsername = app.authorizationService.getUsername();
            //   $('#logged-user').text('Welcome, ' + loggedUsername);

            $('#header-deliver').text("Return Delivery");
            $('#footer-deliver').text("Return Delivery");

            let i = 1;

            for (let elem of data)
            {
                let uniqueElementId = elem['id'];

                $('.all-waiting-orders')
                    .append('<tr>'
                        + '<td class="order-id" style="display:none;">' + elem['id'] + '</td>'
                        + '<td class="sweatshirt-id">' + elem['sweatshirtId'] + '</td>'
                        + '<td class="sweatshirt-name">' + elem['sweatshirtName'] + '</td>'
                        + '<td class="size">' + elem['size'] + '</td>'
                        + '<td class="name">' + elem['name'] + '</td>'
                        + '<td class="address">' + elem['address'] + '</td>'
                        + '<td class="email">' + elem['email'] + '</td>'
                        + '<td class="city">' + elem['city'] + '</td>'
                        + '<td class="number">' + elem['number'] + '</td>'
                        + '<td class="text-center">'
                        + '<div class="d-flex justify-content-center">'
                        + '<input id="' + uniqueElementId + '"type="checkbox" name="deliver" value="true" checked>'
                        + '</div>'
                        + '</td>'
                        + '</tr>');

                $('#' + uniqueElementId).click(function (e)
                {

                    $('#return-waiting-modal').modal();

                    $('#approve-waiting-button').click(function (e)
                    {
                      //  $('#return-waiting-modal').hide();
                        $.ajax({
                            type: 'POST',
                            url: constants.serviceUrl + '/orders/remove-delivered-flag?id=' + uniqueElementId,
                            headers:
                            {
                                'Content-Type': 'application/json'
                                //      'Authorization': app.authorizationService.getCredentials()
                            }
                        }).done(function (data)
                        {
                            let deliveredSize = parseInt($("#delivered-size").text());
                            $("#delivered-size").text(deliveredSize-1);
                            let waitingSize = parseInt($("#waiting-size").text());
                            $("#waiting-size").text(waitingSize+1);
                            setTimeout(function()
                            {
                                app.router.reload('#/orders/delivered');
                            }, 150);

                        }).fail(function (err)
                        {
                            alert("FAIL");
                            console.log(err);
                        }).always(function ()
                        {
                             $('#return-waiting-modal').modal('hide');
                        });

                    });

                    $('#approve-deliver-modal').on('hidden.bs.modal', function ()
                    {
                        //  alert("Hidden."); // this alert shows many times?
                        $('#' + uniqueElementId).prop('checked', true);
                    })

                });

                i++;
            }

            var table = $('#orders-table').DataTable();

            $('#orders-table tbody').on( 'click', 'tr', function ()
            {
                $(this).toggleClass('selected');
            });

            $('#pdf-selected').click( function ()
            {
                let rows = table.rows('.selected').data();

                for(let i=0;i<rows.length;i++)
                {
                    $('.temp-table-data')
                        .append('<tr>'
                            + '<td>' + rows[i][0] + '</td>'
                            + '<td>' + rows[i][1] + '</td>'
                            + '<td>' + rows[i][2]  + '</td>'
                            + '<td>' + rows[i][3]  + '</td>'
                            + '<td>' + rows[i][4]  + '</td>'
                            + '<td>' + rows[i][5]  + '</td>'
                            + '<td>' + rows[i][6]  + '</td>'
                            + '<td>' + rows[i][7]  + '</td>'
                            + '<td>' + rows[i][8]  + '</td>'
                            + '</tr>');
                }

                var doc =new jsPDF();
                doc.autoTable({ html: '#temp-table' });
                doc.save('Selected-Delivered-Orders.pdf');
                doc.languages
                $('#temp-table tbody').empty();
            });

            $('#pdf-all').click( function ()
            {
                var doc =new jsPDF();
                doc.autoTable({ html: '#orders-table' });
                doc.save('All-Delivered-Orders.pdf');
            });

            $('#delete-selected').click( function ()
            {
                let rows = table.rows('.selected').data();
                var selectedRows = new Array();

                for(let i=0;i<rows.length;i++)
                {
                    selectedRows[i] = rows[i][0];
                }

                $.ajax({
                    type: 'POST',
                    url: constants.serviceUrl + '/orders/remove-selected?selectedOrders=' + selectedRows,
                    headers:
                        {
                        'Content-Type': 'application/json',
                        'Authorization': app.authorizationService.getCredentials()
                    }
                }).done(() =>
                {
                    let deliveredSize = parseInt($("#delivered-size").text());
                    $("#delivered-size").text(deliveredSize-selectedRows.length);
                    alert('Success');
                    app.router.reload('#/orders/delivered');
                }).fail((err) =>
                {
                    console.log(err);
                    alert("Error: " + err);
                })
            });
        });
    }).fail((err) => {
        console.log(err);
    });
});

app.router.on("#/cart", null, function ()
{
    // loadNavbar();
    $.ajax(
        {
            type: 'GET',
            url: constants.serviceUrl + '/cart?username=' + app.authorizationService.getUsername(),
            // url: constants.serviceUrl + '/cart?username=' + app.authorizationService.getUsername(),
            headers: {
               'Content-Type': 'application/json',
               // 'Authorization': app.authorizationService.getCredentials()
        }
        }).done((data) => {
            app.templateLoader.loadTemplate('.app',
                'cart', function ()
            {
                var sum = 0;
                for (let elem of data)
                {
                    let sizeSelect = elem['id'];
                    let fullSize = '';
                    let total = 'total' + elem['id'];
                    let quantity = 'quantity' + elem['id'];
                    let removeSavedOrder = 'remove' + elem['id'];
                    let selectedSize = elem['size'];
                    let options = '';
                    if(selectedSize === null)
                    {
                        options = '<option value=""> Величина </option>';
                    }
                    else
                    {
                        options = '<option value="' + elem['size'] + '">' + elem['size'] + '</option>';
                    }

                    // sum += $('#' + total).text;

                    $('#list-orders')
                        .append('<tr>'
                            + '<td class="col-sm-8 col-md-6">'
                            + '<div class="media">'
                            + '<a class="thumbnail pull-left" href="#"> '
                            + '<img class="media-object" src=' + elem['sweatshirtImg1'] + ' style="width: 72px; height: 72px;"> </a>'
                            + '<div class="media-body ml-3">'
                            + '<h4 class="media-heading"><a href="#/sweatshirts/product?id=' + elem['sweatshirtId'] + '">' + elem['sweatshirtName'] + '</a></h4>'
                            // + '<h5 class="media-heading"> Size: ' + elem['size'] + '</h5>'
                            // +           '<div class="row">'
                            // +               '<span class="ml-3">Size: </span>'
                            +               '<select class="form-control w-50 h-25" id="' + sizeSelect + '" required> </select>'
                            // +           '</div>'
                            + '<span>Availability: </span><span class="text-success"><strong>' + elem['sweatshirtRAvailability'] + '</strong></span>'
                            + '</div>'
                            + '</div>'
                            + '</td>'
                            + '<td class="col-sm-1 col-md-1" style="text-align: center">'
                            + '<input type="email" class="form-control" id="' + quantity + '" value="1">'
                            + '</td>'
                            + '<td class="col-sm-1 col-md-1 text-center"><strong>' + elem['sweatshirtNewPrice'] + '</strong></td>'
                            + '<td class="col-sm-1 col-md-1 text-center"><strong id="' + total + '"></strong></td>'
                            + '<td class="col-sm-1 col-md-1">'
                            + '<button id="' + removeSavedOrder + '" type="click" class="btn btn-danger">'
                            + '<span class="glyphicon glyphicon-remove"></span> Remove'
                            + '</button></td>'
                            + '</tr>');

                    for(let size of elem['sweatshirtSizes'])
                    {
                        fullSize += size;

                        if(size != 'X')
                        {
                            if(fullSize === elem['size'])
                            {
                                fullSize = '';
                                continue;
                            }

                            options += '<option value="' + fullSize + '">' + fullSize + '</option>';
                            fullSize = '';
                        }
                    }

                    $('#' + removeSavedOrder).click(function()
                    {
                        $('#approve-remove').modal();

                        $('#approve-button').click(function (e)
                        {
                            $.ajax({
                                type: 'POST',
                                url: constants.serviceUrl + '/delete-saved-order?id=' + elem['id'],
                                headers: {
                                    'Content-Type': 'application/json',
                                    //   'Authorization': app.authorizationService.getCredentials()
                                }
                            }).done((data, request) => {
                                let cartSize = parseInt($("#cart-size").text());
                                $("#cart-size").text(cartSize-1);
                                $('#approve-remove').modal('hide')
                                setTimeout(function(){ app.router.reload('#/cart'); }, 150);
                            }).fail((err) =>
                            {
                                alert('Error while deleting from cart' + err.responseText);
                                console.log(err);
                            });
                        });
                    });

                    $('#' + sizeSelect).html(options);
                    $('#' + total).text(elem['sweatshirtNewPrice'] * $('#' + quantity).val());
                    $('#' + quantity).change(function () {
                        sum -= +$('#' + total).text();``
                        let sumInRow = elem['sweatshirtNewPrice'] * $('#' + quantity).val();
                        $('#' + total).text(sumInRow);
                        sum += +sumInRow;
                        $('#order_summary').text(sum);
                    });
                    sum += +$('#' + total).text();
                }

                $('#order_summary').text(sum);
            });
        }).fail((err) => {
            alert('E R R');
            console.log(err.responseText);
        });
});

app.router.on("#/users/login", null, function ()
{
    loadNavbar();
    app.templateLoader.loadTemplate('.app', 'login', function () {
        document.getElementById("registered_successfully").hidden = hiddenFlag;
        hiddenFlag = true;
        $('#login-user').click(function (e)
        {
            loginMain();
        });
    });
});

app.router.on("#/users/register", null, function ()
{
    loadNavbar();
    app.templateLoader.loadTemplate('.app', 'register', function () {
        (function() {
            'use strict';
            var forms = document.getElementsByClassName('needs-validation');
            var validation = Array.prototype.filter.call(forms, function(form) {
                form.addEventListener('submit', function(event) {
                    if (form.checkValidity() === false) {
                        form.classList.add('was-validated');
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    else {
                        event.preventDefault();
                        event.stopPropagation();

                        var formData = new FormData($('.needs-validation')[0]);

                        let firstName = $('#firstName').val();
                        let lastName = $('#lastName').val();
                        let username = $('#username').val();
                        let email = $('#email').val();
                        let birthDate = $('#datetimepicker').val();
                        let password = $('#password').val();
                        let confirmPassword = $('#confirmPassword').val();
                        var birthDateFormat = new Date(birthDate);

                        $.ajax({
                            type: 'POST',
                            url: constants.serviceUrl + '/users/register',
                            headers: {
                                'Content-Type': 'application/json',
                                'Authorization': app.authorizationService.getCredentials()
                            },
                            data: JSON.stringify({
                                "firstName": firstName,
                                "lastName": lastName,
                                "username": username,
                                "email": email,
                                "birthDate": birthDateFormat,
                                "password": password,
                                "confirmPassword": confirmPassword
                            })
                        }).done((data, request) => {
                            hiddenFlag = false;
                            window.location.href = '#/users/login';
                        }).fail((err) => {
                            if(err.responseText === 'Error: Username already exists. Please try with another one.') {
                                $("#username").val('');
                                document.getElementById("error_register").textContent = "Username already exists.";
                            }
                            else if(err.responseText === 'Error: Passwords do not match!') {
                                document.getElementById("error_register").textContent = "Passwords did not match.";
                            }
                            console.log(err);
                            alert(err.responseText);
                            document.getElementById("error_register").hidden = false;
                            $("#password").val('');
                            $("#confirmPassword").val('');
                        });

                        form.classList.remove('was-validated');

                    }
                }, false);
            });
        })();
    });
});

app.router.on("#/users/logout", null, function () {
    app.authorizationService.evictCredentials();
    // loggedIn = false; TODO: SHOULD THIS BE SET?
    window.location.href = '#/users/login';
});

window.location.href = '#/';