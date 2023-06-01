import axios from 'axios'
const requests = axios.create({
    baseURL:"http://10.24.3.53:8080",
    // 请求超时的时间
    timeout:5000,
});
 
export default requests;
