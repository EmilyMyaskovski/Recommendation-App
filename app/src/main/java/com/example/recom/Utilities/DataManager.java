package com.example.recom.Utilities;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.recom.Models.Recom;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class DataManager extends AppCompatActivity{

    public static ArrayList<Recom> getRecoms() {
        ArrayList<Recom> recoms = new ArrayList<>();

        recoms.add(new Recom()
                .setOverview("קוקוסים גדולים ומלאים במים, טריים כאילו נקטפו לפני דקה אך זמני ההגעה ארוכים")
                .setTitle("קוקוס טרי")
                .setCategory("Food")
                .setUsername("orianzaw10")
                .setPostedOn("20/07/2024")
                .setLink("https://www.tropifarm.co.il/")
                .setRating(4)
                .setImage("https://static.wixstatic.com/media/9ef445_6983590131ae49df84375806c90b81e5~mv2.jpg/v1/fit/w_2500,h_1330,al_c/9ef445_6983590131ae49df84375806c90b81e5~mv2.jpg"));

        recoms.add(new Recom()
                .setOverview("This physical sunscreen lotion absorbs easily and is free of added fragrance. Although it can feel greasy and leave behind a white cast, it’s less visible on skin than most physical sunscreens.")
                .setTitle("Blue Lizard Sensitive Mineral Sunscreen SPF 50+")
                .setCategory("Care and beauty")
                .setUsername("dr.Social")
                .setPostedOn("25/07/2024")
                .setLink("https://www.amazon.com/dp/B083VY5GVN/?tag=thewire06-20&linkCode=xm2&ascsubtag=F0401J0495BBG3CGTP1X7D74KN2CF")
                .setRating(5)
                .setImage("https://m.media-amazon.com/images/I/71yX4MQVp8L._SL1500_.jpg")
        );

        recoms.add(new Recom()
                .setOverview("סופר-פארם הביאה לישראל בשורה צרכנית וקונספט קמעונאי חדשני המשלב בין בית מרקחת למחלקות קוסמטיקה טואלטיקה ותינוקות. כל זאת בשעות פעילות גמישות ובלב שכונות מגורים. מאז ועד היום פועלת הרשת להעניק ללקוחותיה חוויית קנייה ייחודית המשלבת מקצועיות, שירות, מגוון רחב של מוצרים ומבצעים אטרקטיביים המתחלפים לעתים תכופות. כל זאת בחנות מטופחת ובאווירת קנייה נעימה הגורמת ללקוחות \"כבר להרגיש יותר טוב\". בסופרפארם אאוטלט ניתן למצוא את המוצרים של הרשת במחירי אאוטלט")
                .setTitle("סופר פארם אאטולט")
                .setCategory("SALE")
                .setUsername("orianzaw10")
                .setPostedOn("26/07/2024")
                .setLink("https://maps.app.goo.gl/ijxXxcgUCsa6An1F7")
                .setRating(5)
                .setImage("https://myofer.co.il/_next/image?url=https%3A%2F%2Fmedia1.groo.co.il%2Fimage%2Fupload%2Fc_scale%2Cw_130%2Ch_130%2F%2Fv1655638777%2Fprod%2Fsuppliers%2Flogo%2Fsuperpharm(1)-629ddf2fd3009-62af0af8cca54.jpg&w=256&q=100")
        );

        recoms.add(new Recom()
                .setOverview("The #1 moisturizer. Refreshing moisture in a gel texture, fat-free for 100 hours. Keeps the skin saturated with moisture and gives volume and a natural and healthy glow. Safe to use even for sensitive skin.")
                .setTitle("Moisture Surge™ 100H")
                .setCategory("Care and beauty")
                .setUsername("dr.Social")
                .setPostedOn("01/08/2024")
                .setLink("https://www.clinique.co.il/product/1667/83690/moisture-surgetm-100h--1---100-")
                .setRating(5)
                .setImage("https://www.clinique.co.il/media/export/cms/products/1200x1500/cl_sku_KWW401_1200x1500_0.png")
        );

        recoms.add(new Recom()
                .setOverview("המרקם של הג׳ל מאוד נעים על העור ומותיר אותו לחותי ורך. יחד עם זאת, לפחות לעור שלי (רגיל נוטה ליבש) הוא ממש לא מספק 100 שעות לחות. שעות בודדות לאחר השימוש אני מרגישה את העור יחסית יבש (בהשוואה לתכשירי לחות אחרים) כך שהתכשיר אינו עוזר לעור שלי לשמר לחות. בנוסף, התכשיר מגיע בצנצנת, פחות נוח וסטרילי לשימוש (מצריך ממני להשתמש באפליקטור כי אני לא מעונינת להכניס אצבע לצנצנת ולנקות אותו לפני ואחרי) ומקשה על הערכת כמות המוצר (בשונה מבקבוק עם משאבה לחיצה). הסיבה שכן אמליץ עליו היא כי אני חושבת שהוא מאוד מתאים לעור רגיל, מעורב ושומני - המרקם שלו קליל, נראה לי שיהיה נוח להתאפר כאשר הוא משמש בסיס (כי הוא לא מכביד על העור). אותי הוא משרת בקיץ בימים מאוד לחים (אני גרה במרכז) אשר מזיעים בהם הרבה ואני לא זקוקה לקרם לחות כבד יותר, במיוחד לאור העובדה שאני משתמשת בקרם הגנה לאחר מכן ומחדשת כל 3 שעות לאורך היום. כך שהשימוש שלי בו הוא בבקרים כבסיס לקרם ההגנה.")
                .setTitle("מותיר את העור רך ונעים")
                .setCategory("Care and beauty")
                .setUsername("ש92")
                .setPostedOn("01/08/2024")
                .setLink("https://www.clinique.co.il/product/1667/83690/moisture-surgetm-100h--1---100-")
                .setRating(5)
                .setImage("https://www.clinique.co.il/media/export/cms/products/1200x1500/cl_sku_KWW401_1200x1500_0.png")
        );

        recoms.add(new Recom()
                .setOverview("With its powerful hardware, revolutionary controller, and a good library of desirable console-exclusive titles, the PS5 takes the top spot as the very best gaming console that money can buy right now. ")
                .setTitle("PlayStation 5 The best gaming console")
                .setCategory("Gaming")
                .setUsername("best-consoles")
                .setPostedOn("01/08/2024")
                .setLink("")
                .setRating(4.5f)
                .setImage("https://www.officedepot.co.il/media/catalog/product/cache/7d6f95f15bfcd47a35db4e698b07814c/4/7/4707055_.jpg")
        );

        recoms.add(new Recom()
                .setOverview("We came 30 minutes ahead of our reservation with two little children. The person at the entrance told us that he can’t accept us ahead of time because ‘he tries to provide personal service to his customers’ and does not want to ‘overfill’ the place. There were a lot of vacant tables, neither we delayed the entry of anyone with an earlier reservation than ours. I even said that it was ok for us to wait while sitting inside at the table just in case their cook is overloaded. Yet the guy declined our request to enter and we had to wait outside. Note: no security or sanitary reasons were in force so it’s just the matter of their own ‘policy’. We felt really upset and left the place.")
                .setTitle("Nini Hachi")
                .setCategory("Food")
                .setUsername("Alex K")
                .setPostedOn("01/08/2024")
                .setLink("https://www.google.com/maps/dir//32.092144,34.7746/@32.0920673,34.6921827,12z?entry=ttu")
                .setRating(1)
                .setImage("https://dynamic-media-cdn.tripadvisor.com/media/photo-o/1b/d1/ea/71/outside-image-of-the.jpg?w=300&h=200&s=1")
        );

        recoms.add(new Recom()
                .setOverview("The queue was extremely long, but as my sons have a nimbus card we were allowed to use the accessibility line which really helped. The staff members we met were all friendly, helpful and polite. I do think they put too many people into a pod, (but can understand why) as wasn't able to move freely round as everyone was crowded in the same area and my children couldn't always see out. However it was a good experience and they enjoyed going so high up.")
                .setTitle("Good experience but pod seemed like too many people")
                .setCategory("Travel")
                .setUsername("kabarnes2023")
                .setPostedOn("01/08/2024")
                .setLink("https://www.google.com/maps/place/Riverside+Building,+County+Hall,+Westminster+Bridge+Rd,+London+SE1+7JA,+UK/@51.5031897,-0.1220941,17z/data=!3m1!4b1!4m6!3m5!1s0x487604c7c7eb9be3:0x3918653583725b56!8m2!3d51.5031864!4d-0.1195192!16s%2Fg%2F11b62lft4n?entry=ttu")
                .setRating(5)
                .setImage("https://www.londoneye.com/media/uzzjd1wi/sunset-ticket-imagery.jpg?anchor=center&mode=crop&format=webp&quality=80&width=600&height=300")
        );


        return recoms;
    }




}
