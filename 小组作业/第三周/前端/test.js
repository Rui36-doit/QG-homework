new Vue({
    el:"#app",
    data(){
        return{
            count:1,
            type: "login",
            login: {username: "", password: "", warn: 1},
            register: {username: "", password: "", passwordagain: "", warning1: 0, warn: 1},
            warn1: 1,
            student:{name: "", age: 0, id: 0, phonenumber: "", password: "", classnum: 0},
            UI:{loginissee: true, inforissee: false, classsee: false, getinforsee: false},
            classes:[]
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
        //对比注册段密码一不一样
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
        isEmptyregister(){
            if(this.register.password === '' || this.register.username === '' || this.register.passwordagain === ''){
                return true;
            }else{
                return false;
            }
        },
        //提交注册时的数据
        doregister(){
            if(this.isEmptyregister()===false&&
                this.register.password===this.register.passwordagain){
                alert("注册成功");
                this.UI.loginissee = false;
                this.count = 3;
                this.UI.getinforsee = true;
            }else{
                alert("输入格式不对");
            }
        },
        //检查账号符不符合格式
        checkname(name) {
            const pattern = /^\d+$/;
            if (pattern.test(name) || name === '') {
                this.warn1 = 1;
                return true;
            } else {
                this.warn1 = 0;
                return false;
            }
        },
        //提交登录信息
        submitinfor() {
            if(this.login.password === '' || this.login.username === '' || this.warn1 === 0){
                alert("输入格式不对");
            }else {
                var logindate = {
                    "username": this.login.username,
                    "password": this.login.password,
                }
                axios.post("http://localhost:8080/homework_war/myservelt", logindate,{headers: { 'X-Action': 'login' }}).
                then(resp => {
                    //alert(resp.data);
                    if(resp.data === null){
                        alert("你的账号不存在或密码错误");
                    }else{
                        //隐藏登录表单
                        this.UI.loginissee = false;
                        this.count = 3;
                        //展示学生信息表单
                        var name = resp.data.name;
                        alert("你的账号是"+resp.data + "，务必牢记");
                        this.student.name = name;
                        this.student.id = resp.data.id;
                        this.student.age = resp.data.age;
                        this.student.phonenumber = resp.data.phonenumber;
                        this.UI.inforissee = true;
                    }
                })
            }
        },
        backinfor(){
            this.UI.classsee = false;
            this.UI.inforissee = true;
        },
        backtologin(){
            this.UI.getinforsee = false;
            this.UI.loginissee = true;
            this.count = 1;
            this.type = "login";
        },
        seeclasses(){
            axios.post("http://localhost:8080/homework_war/myservelt",{},{headers: { 'X-Action': 'seeclasses' }}).
            then(resp => {
                this.classes = resp.data;
                this.UI.classsee = true;
                this.UI.inforissee = false;
            })
        },
        //提交完善的信息
        submitregister(){
            var flag = 0;
            if((this.student.age > 0 && this.student.age < 100) && this.checkname(this.student.phonenumber) === true
                && this.student.phonenumber !== ""){
                flag = 1;
            }else{
                flag = 0;
            }
            if(flag === 0){
                alert("你输入的格式存在问题");
            }else{
                this.student.name = this.register.username;
                this.student.password = this.register.password;
                alert(this.student.name + " " + this.student.password + " " + this.student.age + " " + this.student.phonenumber);
                //提交数据
                axios.post("http://localhost:8080/homework_war/myservelt", this.student, {headers: { 'X-Action': 'register'}})
                this.student.name = "";
                this.student.age = null;
                this.student.password = "";
                this.student.phonenumber = "";
            }
        }
    }
});
