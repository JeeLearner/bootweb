/**
 *
=======================1.series========================================
roam:鼠标移动与漫游是否开启，默认false
zoom:当前视角的缩放比例,默认1，一般设置1.2
center:当前视角的中心点，用经纬度表示。如center: [115.97, 29.71]
aspectScale: 0.75, //长宽比
label: {
    normal: { //地图区域~~名称直接显示在map块中
        show: false //是否显示
    },
    emphasis: { //悬浮区~~鼠标放在map块中显示的内容
        show: false,
        textStyle: {
            color: '#fff'
        }
    }
},
itemStyle: { //地图区域的多边形 图形样式。
    normal: {
        areaColor: '#3a7fd5',
        borderColor: '#0a53e9',//线颜色
        shadowColor: '#092f8f',//外发光,阴影颜色
        shadowBlur: 20 //图形阴影的模糊大小
    },
    emphasis: {
        areaColor: 'red',//悬浮区~~背景颜色
    }
}
geoIndex: 0, 当设定了 geoIndex 后，series-map.map 属性，以及 series-map.itemStyle 等样式配置不再起作用，而是采用 geo 中的相应属性。
showLegendSymbol:false 在图例相应区域显示图例的颜色标识（系列标识的小圆点），存在 legend 组件时生效。
animation: false,  //图例翻页是否使用动画。

-----------------------type: 'scatter' 散点（气泡）图-------------
coordinateSystem:geo 该系列使用的坐标系，可选：
  'cartesian2d'使用二维的直角坐标系，通过 xAxisIndex, yAxisIndex指定相应的坐标轴组件。
  'polar'使用极坐标系，通过 polarIndex 指定相应的极坐标组件
  'geo'使用地理坐标系，通过 geoIndex 指定相应的地理坐标系组件。
symbolSize:10 标记的大小，可以设置成诸如 10 这样单一的数字，也可以用数组分开表示宽和高，例如 [20, 10] 表示标记宽为20，高为10。
label: 图形上的文本标签
label.formatter 标签内容格式器，支持字符串模板和回调函数两种形式，字符串模板与回调函数返回的字符串均支持用 \n 换行。
 字符串模板 模板变量有：
  {a}：系列名。 即name
  {b}：数据名。
  {c}：数据值。本处即坐标
  {@xxx}：数据中名为'xxx'的维度的值，如{@product}表示名为'product'` 的维度的值。
  {@[n]}：数据中维度n的值，如{@[3]}` 表示维度 3 的值，从 0 开始计数。
 示例：formatter: '{b}: {@score}'
itemStyle 图形样式。
symbol: 'circle' 标记的图形。
  标记类型包括 'circle', 'rect', 'roundRect', 'triangle', 'diamond', 'pin', 'arrow', 'none'
zlevel:1 用于Canvas分层，zlevel大的Canvas会放在 zlevel小的 Canvas 的上面。
hoverAnimation: true,  是否开启鼠标 hover 的提示动画效果。
showEffectOn: 'render'配置何时显示特效。
 'render' 绘制完成后显示特效。
 'emphasis' 高亮（hover）的时候显示特效。
rippleEffect: 涟漪特效相关配置。
brushType: 'fill' 波纹的绘制方式，可选 'stroke' 和 'fill'。

 =====================================2.visualMap========================
visualMap 是视觉映射组件
    calculable: true 是否显示拖拽用的手柄（手柄能拖拽调整选中范围）。

 =====================================3.tooltip========================
 tooltip提示框组件


 *
 *
 *
 *
 */
    //地图
var dom_map = document.getElementById("map");
var mapChart = echarts.init(dom_map);

var chinaJsonUrl = "/data/china.json";
$.get(chinaJsonUrl, function (chinaJson) {
    echarts.registerMap('china', chinaJson);
    var mapOption = {
        title: {
            text: '访客分布图',
            subtext: '',
            show: true,
            top: 20,
            x: 'center',
            textStyle: {
                color: '#000000'
            }
        },
        backgroundColor: {
            type: 'linear', //线性渐变，前四个参数分别是 x0, y0, x2, y2, 范围从 0 - 1，相当于在图形包围盒中的百分比.
            x: 0,
            y: 0,
            x2: 1,
            y2: 1,
            colorStops: [{
                offset: 0, color: '#f3f3f4' // 0% 处的颜色
            }, {
                offset: 1, color: '#f3f3f4' // 100% 处的颜色
            }],
            globalCoord: false // 缺省为 false，如果 globalCoord 为 `true`，则该四个值是绝对的像素位置
        },
        legend: { //这里没用
            orient: 'vertical',
            y: 'bottom',
            x: 'right',
            data: ['pm2.5'],
            textStyle: {
                color: 'red'
            }
        },
        visualMap: {
            show: true,
            min: 0,
            max: 300,
            left: 'left',
            top: 'bottom',
            text: ['高', '低'], // 文本，默认为数值文本
            calculable: true,
            seriesIndex: [2], //这里可以写多个，如1,2
            inRange: {
                color: ['#2EF506', '#F9EA05', '#FF0000'],
                symbolSize: [40, 40]
            }
        },
        tooltip: {
            trigger: 'item',
            formatter: function (params) {
                var pre = '地区：'+params.name + '<br/>' + '人数：';
                if(typeof(params.value)[2] == "undefined"){
                    return pre + params.value;
                }else{
                    return pre + params.value[2];
                }
            }
        },
        geo: {
            map: 'china',
            show: true,
            roam: true,
            zoom: 1.6,
            center: [106, 36],
            label: {
                normal: {
                    show: false
                },
                emphasis: {
                    show: false,
                }
            },
            itemStyle: { //地图区域的多边形 图形样式。
                normal: {
                    areaColor: '#3a7fd5',
                    borderColor: '#0a53e9',//线颜色
                    shadowColor: '#092f8f',//外发光,阴影颜色
                    shadowBlur: 20 //图形阴影的模糊大小
                },
                emphasis: {
                    areaColor: '#0a2dae',//悬浮区背景
                }
            }
        },
        series: [
            {
                name: 'china map',
                type: 'map',
                map: 'china',
                roam: true,
                aspectScale: 0.75,
                geoIndex: 0,
                showLegendSymbol:false,
                animation: false,
                label: {
                    normal: {
                        show: false
                    },
                    emphasis: {
                        show: false,
                        textStyle: {
                            color: '#fff'
                        }
                    }
                },
                itemStyle: {
                    normal: {
                        areaColor: '#031525',
                        borderColor: '#FFFFFF',
                    },
                    emphasis: {
                        areaColor: '#2B91B7'
                    }
                },
                data: []
            },
            {
                name: 'light',
                type: 'scatter',
                coordinateSystem: 'geo',
                symbolSize: 5,
                label: { //标签样式  新疆
                    normal: {
                        formatter: '{b}',
                        position: 'right',
                        //color: 'blue',
                        show: true
                    },
                    emphasis: {
                        show: true
                    }
                },
                itemStyle: {  //图形样式  标签旁边的散点图标
                    normal: {
                        color: '#fff'
                    }
                },
                data: [],

            },
            {
                name: 'scaval',
                type: 'scatter',
                coordinateSystem: 'geo',
                symbol: 'pin',
                symbolSize: [50,50],
                zlevel: 1,
                hoverAnimation: true,
                showEffectOn: 'render', //type=effectScatter使用，这里无效
                rippleEffect: { //同上
                    brushType: 'stroke'
                },
                label: {  //标签
                    normal: {
                        show: true,
                        textStyle: {
                            color: '#fff',
                            fontSize: 9,
                        },
                        formatter: function(value){
                            return value.data.value[2]
                        }
                    }
                },
                itemStyle: { //图形样式，这里是pin图
                    normal: {
                        color: '#D8BC37', //标志颜色
                    }
                },
                data: [],
            },
        ]
    };
    var geoCoordMap = {
        '台湾': [121.5135, 25.0308],
        '黑龙江': [127.9688, 45.368],
        '内蒙古': [110.3467, 41.4899],
        "吉林": [125.8154, 44.2584],
        '北京': [116.4551, 40.2539],
        "辽宁": [123.1238, 42.1216],
        "河北": [114.4995, 38.1006],
        "天津": [117.4219, 39.4189],
        "山西": [112.3352, 37.9413],
        "陕西": [109.1162, 34.2004],
        "甘肃": [103.5901, 36.3043],
        "宁夏": [106.3586, 38.1775],
        "青海": [101.4038, 36.8207],
        "新疆": [87.9236, 43.5883],
        "西藏": [91.11, 29.97],
        "四川": [103.9526, 30.7617],
        "重庆": [108.384366, 30.439702],
        "山东": [117.1582, 36.8701],
        "河南": [113.4668, 34.6234],
        "江苏": [118.8062, 31.9208],
        "安徽": [117.29, 32.0581],
        "湖北": [114.3896, 30.6628],
        "浙江": [119.5313, 29.8773],
        "福建": [119.4543, 25.9222],
        "江西": [116.0046, 28.6633],
        "湖南": [113.0823, 28.2568],
        "贵州": [106.6992, 26.7682],
        "云南": [102.9199, 25.4663],
        "广东": [113.12244, 23.009505],
        "广西": [108.479, 23.1152],
        "海南": [110.3893, 19.8516],
        '上海': [121.4648, 31.2891],
    };
    // 北京： [116.4551, 40.2539] ==> 北京：199
    var convertData = function(data) {
        var res = [];
        for (var i = 0; i < data.length; i++) {
            var geoCoord = geoCoordMap[data[i].name];
            if (geoCoord) {
                res.push({
                    name: data[i].name,
                    value: geoCoord.concat(data[i].value) //[116.4551, 40.2539,199]
                });
            }
        }
        return res;
    };
    //数据准备
    var data = [
        {name:"北京",value:199},
        {name:"天津",value:42},
        {name:"河北",value:102},
        {name:"山西",value:81},
        {name:"内蒙古",value:47},
        {name:"辽宁",value:67},
        {name:"吉林",value:82},
        {name:"黑龙江",value:123},
        {name:"上海",value:24},
        {name:"江苏",value:92},
        {name:"浙江",value:114},
        {name:"安徽",value:109},
        {name:"福建",value:116},
        {name:"江西",value:91},
        {name:"山东",value:119},
        {name:"河南",value:137},
        {name:"湖北",value:116},
        {name:"湖南",value:114},
        {name:"重庆",value:91},
        {name:"四川",value:125},
        {name:"贵州",value:62},
        {name:"云南",value:83},
        {name:"西藏",value:9},
        {name:"陕西",value:80},
        {name:"甘肃",value:56},
        {name:"青海",value:10},
        {name:"宁夏",value:18},
        {name:"新疆",value:180},
        {name:"广东",value:123},
        {name:"广西",value:59},
        {name:"海南",value:14},
    ];
    mapOption.series[0].data = data;
    mapOption.series[1].data = convertData(data);
    mapOption.series[2].data = convertData(data);
    mapChart.setOption(mapOption);
});

