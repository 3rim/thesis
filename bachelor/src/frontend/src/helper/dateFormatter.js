const myPlugin = {
    install(app) {
        //Format ISO-Standard of LocalDate (yyyy-MM-dd) to german standarf (dd.mm.yyyy)
        app.config.globalProperties.$dateArrayToGermanFormat = (key) => {
            if( typeof key === 'string' && key!== 'null' && key ){
                const dateArray = key.split("-");
            var dateObject = {
                day: dateArray[2],
                month: dateArray[1],
                year: dateArray[0],
            }
            return dateObject
            }
            else{
                return ""
            }
        },
        app.config.globalProperties.$dateStringToGermanFormat = (key) => {
            if( typeof key === 'string' && key !== 'null' && key ){
            const dateArray = key.split("-");
            var str = dateArray[2]+'.'+dateArray[1]+'.'+dateArray[0]
            return str
            }
            else{
                return ""
            }
        }
    }
}
 
export default myPlugin;