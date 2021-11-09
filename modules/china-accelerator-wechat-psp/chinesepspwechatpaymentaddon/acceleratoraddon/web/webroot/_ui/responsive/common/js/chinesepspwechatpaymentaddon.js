ACC.payment.wechatpay = {
    init: function(){
        $('#payRightNowButton').removeAttr('target');
        $('#payRightNowButton').removeAttr("data-href");
    },
    showWechatpay: function(){
        if(/micromessenger/i.test(navigator.userAgent.toLowerCase())){
            $('li#wechatpay').show();
        }
    },
    doPayment: function(orderCode){
        $.get(ACC.config.encodedContextPath + '/checkout/multi/wechat/pay/' + orderCode,
            function(data){
                ACC.payment.wechatpay.onBridgeReady(data, orderCode);
            }
        );
    },
    onBridgeReady: function(data, orderCode){
        WeixinJSBridge.invoke('getBrandWCPayRequest', {
            'appId' : data.appId,
            'timeStamp' : data.timeStamp,
            'nonceStr' : data.nonceStr,
            'package' : data.packageName,
            'signType' : data.signType,
            'paySign' : data.paySign,
        }, function(resp) {
            var status = resp.err_msg.split(':')[1];
            if (status === 'ok' || status === 'fail'){
                window.location.href = ACC.config.encodedContextPath + '/checkout/multi/summary/checkPaymentResult/' + orderCode;
            }
        });
        $.ajax({
            type: 'POST',
            url: ACC.config.encodedContextPath + '/checkout/multi/wechat/startPay',
            data: {'orderCode' : orderCode}
        });
    }
};

$(function(){
    ACC.payment.wechatpay.showWechatpay();
});
