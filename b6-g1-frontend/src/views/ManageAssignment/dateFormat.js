function DateFormat(postDate) {
    let date =`${postDate}`;
    let Day= new Date(date).getDate();
    let Month = new Date(date).getMonth();
    let Year = new Date(date).getFullYear();
    let newDateFormat = `${Day}/${Month}/${Year}`;
    return newDateFormat
}
export default DateFormat