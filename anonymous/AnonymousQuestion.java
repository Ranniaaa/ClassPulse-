package anonymous; 
// هذا السطر يحدد أن هذا الكلاس ينتمي إلى folder اسمو anonymous
// لازم يكون نفس اسم الفولدر بالضبط

public class AnonymousQuestion { 
// تعريف الكلاس (الـ blueprint تاع السؤال)

    private int id; 
    // رقم خاص بكل سؤال (معرف فريد)

    private String content; 
    // نص السؤال الذي يكتبه الطالب

    private int votes; 
    // عدد الأصوات (upvotes) التي حصل عليها السؤال

    private boolean answered; 
    // هل تم الرد على السؤال أم لا (true = تم الرد)

    // -------------------------
    // Constructor
    // -------------------------

    public AnonymousQuestion(int id, String content) {
        // هذا constructor يُستعمل لإنشاء سؤال جديد

        this.id = id;
        // نربط المتغير الداخلي بالقيمة المرسلة

        this.content = content;
        // تخزين نص السؤال

        this.votes = 0;
        // عند إنشاء السؤال يبدأ عدد الأصوات بـ 0

        this.answered = false;
        // عند الإنشاء السؤال لم يتم الرد عليه بعد
    }

    // -------------------------
    // Methods
    // -------------------------

    public void upvote() {
        // هذه الدالة تزيد عدد الأصوات

        votes++;
        // زيادة vote واحدة
    }

    public void markAsAnswered() {
        // هذه الدالة تستعمل عندما يرد الأستاذ على السؤال

        answered = true;
        // تغيير الحالة إلى "تم الرد"
    }

    // -------------------------
    // Getters (للاستعمال لاحقاً في الواجهة أو النظام)
    // -------------------------

    public int getId() {
        return id;
        // إرجاع رقم السؤال
    }

    public String getContent() {
        return content;
        // إرجاع نص السؤال
    }

    public int getVotes() {
        return votes;
        // إرجاع عدد الأصوات
    }

    public boolean isAnswered() {
        return answered;
        // إرجاع حالة السؤال (تم الرد أو لا)
    }
}