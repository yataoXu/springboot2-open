$path ="http://127.0.0.1:8080/";
var vm = new Vue({
	el: '#app',
	data: {
		username: '',
		password: '',
		captcha: '',
		rememberMe: false,
		error: false,
		errorMsg: '',
		src: $path+'captcha.jpg?t=' + guid()
	},
	methods: {
        refreshCode: function () {
			this.src = $path+'captcha.jpg?t=' + guid();
		},
		login: function () {
			var data = "username=" + vm.username + "&password=" + vm.password + "&captcha=" + vm.captcha+ "&rememberMe=" + vm.rememberMe;
			$.ajax({
				type: "POST",
				url: $path+"/sys/login",
				data: data,
				success: function (result) {
					console.log(result);
					if (result.code == 0) {//登录成功
						parent.location.href = 'index.html';
					} else {
						vm.error = true;
						vm.errorMsg = result.msg;
						vm.refreshCode();
						vm.password="";
					}
				},error:function(jqXHR){
                    console.log(JSON.stringify(jqXHR));
                }
			});
		}
	}
	
});
function S4() {
    return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
}

function guid() {
    unique = (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
    return unique;
}