/** 결제 **/
    // 결제 금액, 구매자의 이름, 이메일
const priceAmount = $('#totalPrice').val();
const buyerMemberEmail = $('#memberEmail').val();
const buyerMemberName = $('#memberName').val();
// const form = document.getElementById("payment");

console.log(priceAmount);
console.log(buyerMemberName);
console.log(buyerMemberEmail);
const IMP = window.IMP;
IMP.init('imp82533053');

function requestPay() {
    const priceAmount = $('#totalPrice').val();
    const buyerMemberEmail = $('#memberEmail').val();
    const buyerMemberName = $('#memberName').val();

    console.log(priceAmount);
    console.log(buyerMemberName);
    console.log(buyerMemberEmail);
    const IMP = window.IMP;
    IMP.init('imp82533053');

    // IMP.request_pay(param, callback) 결제창 호출
    // 모바일로 결제시 callback함수가 실행되지 않고, m_redirect_url로 리다이렉트 된다.
    IMP.request_pay({ // param
        pg: "kakaopay.TC0ONETIME",
        pay_method: "card",
        merchant_uid: 'cart_' + new Date().getTime(),
        name: "토스페이 테스트",
        amount: priceAmount,
        buyer_email: buyerMemberEmail,
        buyer_name: buyerMemberName,
    }, function (rsp) { // callback
        console.log(rsp);

        /** 결제 검증 **/
        $.ajax({
            type: 'POST',
            url: '/verifyIamport/'+rsp.imp_uid,
            beforeSend: function(xhr){
                xhr.setRequestHeader(header, token);
            }
        }).done(function(result){

            // rsp.paid_amount와 result.response.amount(서버 검증) 비교 후 로직 실행
            if(rsp.paid_amount === result.response.amount){
                alert("결제가 완료되었습니다.");
                $.ajax({
                    type:'POST',
                    url:'/lecture/payment',
                    beforeSend: function(xhr){
                        xhr.setRequestHeader(header, token);
                    }
                }).done(function() {
                    window.location.reload();
                }).fail(function(error){
                    alert(JSON.stringify(error));
                })
            } else{
                alert("결제에 실패했습니다."+"에러코드 : "+rsp.error_code+"에러 메시지 : "+rsp.error_message);

            }
        })
    });
};