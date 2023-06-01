import Mock from 'mockjs' // 1、引入Mock
const Random = require('mockjs').Random;
const data={              
    "title":"@ctitle",
    "image":"@Image('900x900','@color', 'Random Image')",
};

Mock.mock(RegExp('/mock'), () => {
    return data
})