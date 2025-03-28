new Vue({
    el:"#app",
    data(){
        return{
            count:1,
            type: "login",
            login: {username: "", password: "", warn: 1},
            register: {username: "", password: "", passwordagain: "", warning1: 0, warn: 1}
        }
    },
    methods:{
        loginUI(){
            this.count = 1;
            this.type = 'login';
        },
        registerUI(){
            this.count = 2;
            this.type = 'register';
        },
        compare(){
            if(this.register.passwordagain && this.register.password !== this.register.passwordagain){
                this.register.warning1 = 1;
            }else{
                this.register.warning1 = 0;
            }
            if(this.register.username === '' || this.register.password === '' || this.register.passwordagain ===''){
                this.register.warn = 1;
            }else{
                this.register.warn = 0;
            }
        },
        isEmpy(){
            if(this.login.password === '' || this.login.username === ''){
                this.login.warn = 1;
                alert("输入格式不正确");
            }else{
                this.login.warn = 0;
            }
        },
        checkregister(){
            if(this.register.warning1 === 1 || this.register.warn === 1){
                alert("输入格式不正确" + this.register.warning1 + this.register.warn);
            }
        }
    }
});
