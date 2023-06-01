import request from "@/utils/request";
import {BACKEND_URL} from "@/utils/config";

export default {
    login:{
        register(login, mail, password, phone){
            return request.post(BACKEND_URL + '/register', {
                name: login,
                email: mail,
                password: password,
                // phone: phone
            })
        }
    }
}