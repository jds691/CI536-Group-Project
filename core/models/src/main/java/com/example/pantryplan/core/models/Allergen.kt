package com.example.pantryplan.core.models

enum class Allergen {
    MILK,
    TREE_NUTS,
    GLUTEN,
    EGGS,
    PEANUTS,
    FISH,
    MOLLUSCS,
    LUPIN,
    SESAME,
    SOYBEANS,
    CELERY,
    MUSTARD,
    SULPHIDES,
    CRUSTACEANS
    /*
WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWNWNNWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWNKOkOOKXXKdo,;',x0KXNWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
WWNWWNWWNWWNWWWWWWNWWNWWNWWNWWNWNko,....   ....      . .:xOKXK0NWNWWNWWWWWWNWWNWWNWWNWWNWWWWWWNWW
NWWWWWWWWWNWWNWWNWWNWWWWWWWWWNXl,c...                         .;d0KNNXNWWNWWNWWWWWWWWWNWWNWWNWWNW
WWWWWWWWWWWWWWWWWWWWWWWWWX0occ..                                 . . 'OWWWWWWWWWWWWWWWWWWWWWWWWWW
WWNWWNWWNWWWWWWWWWNWWNW0c.                                             lNWWNWWNWWNWWNWWWWWWWWWNWW
NWWWWWWNWWNWWNWWNWWNWNo.                                                dNWWNWWWWWWNWWNWWNWWNWWNW
WWWWWWWWWWWWWWWWWWWWX;..                                                 'xOXWWWWWWWWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWWWW0.                                                      .;oXNWWWWWWWWWWWWWWWWW
NWWWWWWNWWNWWNWWNWK.       .                                                 ..;oWWNWWNWWNWWNWWNW
WWNWWNWWNWWWWWWWWN:..    ...                                                   ..dNWNWWWWWWWWWNWW
WWWWWWWWWWWWWWWWWN'  .  .                                                        :0WWWWWWWWWWWWWW
NWWWWWWNWWNWWNWWNk.   ..     .  ..                                                lXWWNWWNWWNWWNW
WWNWWNWWNWWNWWWWWc    .     .'...'...........                                     dWNWWNWWWWWWNWW
WWWWWWWWWWWWWWWWK'.        ,;:,..,:;:;;;,,....                                     kWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWc  .      .,:clc,;::ccc:::,,,'.    .                               :WWWWWWWWWWWWW
WWNWWNWWNWWNWWWl      ......;ldol:ccccllollll:;'......                             'XWWNWWWWWWNWW
NWWWWWWNWWNWWXd       ......';ldxxdlc:::lodddol:;;.. .  ...                        .kWNWWNWWNWWNW
WWWWWWWWWWWWWW:          ..;ccooxkkkkxdddxxxxxddlcc,.    ....                       .KWWWWWWWWWWW
WWWWWWWWWWWWWWc         ..'cdxkkkOOOkkkkxxxxkkkxxdol:'.  ......                      .xWWWWWWWWWW
NWWWWWWWWWNWWNk        ..'ldkkkOOOOOOOOOOOOOOkkkkkxdol;.   ......                    lKWWNWWNWWNW
WWWWWWWWWWWWWWW.      ..:oxxkkOOOOOOOOOOOOOOOOOOkkxxddolc;'..........                'dONWWWWWWWW
WWWWWWWWWWWWWWW;      .:dxxxkkOOOOOOOOOOOOOkOOOOkxxxxxxddolc:;'......                 .xNWWWWWWWW
NWWWWWWNWWNWWNNo     ..oxxxxkkkkkkkkkOOOOOkkOOkkkxxxxxxxdolllc:;,,,,'.               'KWWNWWNWWNW
WWNWWNWWNWWNWWo.      .dxxxdddooc:;;:clodkkkOkdolc:;;,,:cc:::::;,,,;,..              'ONWWWWWWNWW
WWWWWWWWWWWWWWO.      ,xxxxxxolc';.',;:loxOkOxc,;;,,,,,;;;:ccc:;;;;,,''.             .xK0NWWWWWWW
WWWWWWWWWWWWWWK.     .oxkkkxoodk::,clllxxkOOOd:,,:cclk:c.'',,;c:cc:;,,,'.            .,'cXWWWWWWW
WWNWWNWWNWWNWWW0' ...,dkkOOkkkkOkkxxdxxxxkOOkd:;;lodxkkollll::llllcc:;,'..          .,.oNWNWWWNWW
NWWWWWWWWWNWWNWWl.c:,,okkOOOOOOOOOOkkkkkkkOkxo:;::odxxkxxdoooddddool:;,'.    ....'.....:ONWWNWWNW
WWWWWWWWWWWWWWWWc.'d:,lxkOOOOOO0O000OOkkkOOkdl:;:cldxxkkkkxxdddxddoc:;,'.   .....,.  ..kNWWWWWWWW
WWNWWNWWNWWWWWWWX, dk:cxkOOO00O000000OkkOOOkdc::::ldxkkkkkxxxxxxdolc;,,'..  .','',. .c0NWWWWWWNWW
NWWWWWWNWWNWWNWWNX'd0x;xkOOOO0000000OOkkOOOkdl:;::lxkkOOkkxxxxxdool:;'''. ...,;;';,okKNWWNWWNWWNW
WWWWWWWWWWWWWWWWWWOx0OddkOOO0000000OOOkOOOOkxoc;;;ldkOOOOOkxxxddolc:,''.. ...';:,;,OXWWWWWWWWWWWW
WWWWWWWWWWWWWWWWWWNkOOkxxkOO00000000OOOO00Oxdoc:cc;okOOOOOOkkxdolc:;,''..'...';:;:XNWWWWWWWWWWWWW
NWWWWWWNWWNWWNWWNWWXOO0kxkOOO0000OO00OxkOOOdlcccll;:xkOOOkkxxxdll::;,,'..'''.,;;:KWNWWNWWNWWNWWNW
WWNWWNWWNWWWWWWWWWNW0OOkxkOOOO00OOOOOOOkkxdoc:::::codxkkkkkxxdollc:;,''..',;;,;;0NWWNWWWWWWWWWNWW
WWWWWWWWWWWWWWWWWWWWWK0kxxkkOOOOOOOOOkkkOOxxxdolcldddddxkkxxdolllc:;,,'.';;::;lKWWWWWWWWWWWWWWWWW
NWWWWWWWWWNWWNWWNWWNWWWKxxkkOOOOkkkkkkOOOOkkOkxdollooooodkxddocclc:;,''. ',,.xWWWWWWWWNWWNWWNWWNW
WWNWWNWWNWWNWWWWWWNWWNWWkxkkkkOOkkxxdddxxdxkkxxdoolccclxxxxdol:coc:;;,'.   .0WNWWNWWNWWNWWWWWWNWW
WWWWWWWWWWWWWWWWWWWWWWWWNkxkkkOOOOOOkxdddddooolc::::coxkkxdoc::ll:;:;,'.';kWNWWWWWWNWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWWWWWWWWWWXkkkkkOOkkOOOOOOOOOkkxdoolloddxxxdl::llc:::;,OWWWWWNWWWWWWWWWWWWWWWWWWWW
WWNWWNWWNWWNWWWWWWNWWNWWNWXkkkkkkkOkOOOkkxxdddddoddodooddol::lll:;;,;0WWWWWNWWNWWNWWNWWNWWWWWWNWW
NWWWWWWNWWNWWNWWNWWNWWWWWWNNkkkkkOOkkkOOkkkxkxxxkxdooolodc:cllc:;,;xWWNWWNWWNWWWWWWNWWNWWNWWNWWNW
WWWWWWWWWWWWWWWWWWWWWWWWWWWWW0xkkkOOkOOOOOOkkkkxxxddoolol::lc:;,,kNWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWXOkkOOOOOO0OOOOkkxxxdooollc:::;;lOWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
NWWWWWWWWWNWWNWWNWWNWWWWWWWWWNWWXOkOO0000OOOOOkkxxddolc:;;;d0NWWWWWNWWNWWNWWNWWWWWWWWWNWWNWWNWWNW
WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWXOOOOOOOkkOkkkxddoc:;;oONWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWX0OkddoddooolllooxKNWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW
NWWWWWWNWWNWWNWWNWWNWWWWWWNWWNWWNWWNWWNWWNNWNNWWNWWNWWNWWNWWWWWWNWWNWWNWWNWWNWWWWWWNWWNWWNWWNWWNW
     */
}