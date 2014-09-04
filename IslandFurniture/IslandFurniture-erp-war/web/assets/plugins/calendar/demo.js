$(document).ready( function(){
  var cTime = new Date(), month = cTime.getMonth()+1, year = cTime.getFullYear();

    theMonths = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];

    theDays = ["S", "M", "T", "W", "T", "F", "S"];
    events = [
      [
        "4/"+month+"/"+year, 
        'Marketing Meeting', 
        '#', 
        '#177bbb', 
        'Time: 10:30 <br> Venue: Seminar Room 3'
      ],
      [
        "7/"+month+"/"+year, 
        'Marketing Plans for September Due', 
        '#', 
        '#1bbacc', 
        'Time: 15:30 <br> Details: Proposal to be uploaded'
      ],
      [
        "17/"+month+"/"+year, 
        'Milestone Release', 
        '#', 
        '#fcc633', 
        'Time: 10:30 <br> Venue: Seminar Room 1'
      ],
      [
        "19/"+month+"/"+year, 
        'Corporate Outreach', 
        '#', 
        '#e33244',
        'Time: 10:30 <br> Venue: MapleTree Atrium'        
      ]
    ];
    $('#calendar').calendar({
        months: theMonths,
        days: theDays,
        events: events,
        popover_options:{
            placement: 'top',
            html: true
        }
    });
});