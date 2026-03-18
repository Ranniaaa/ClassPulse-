package anonymous;

public class TestAnonymous {

    public static void main(String[] args) {

        // إنشاء سؤال جديد
        AnonymousQuestion q1 = new AnonymousQuestion(1, "What is OOP?");

        // طباعة المعلومات
        System.out.println("Question: " + q1.getContent());
        System.out.println("Votes: " + q1.getVotes());

        // عمل upvote
        q1.upvote();

        System.out.println("Votes after upvote: " + q1.getVotes());

        // نعلموه بلي تم الرد عليه
        q1.markAsAnswered();

        System.out.println("Answered: " + q1.isAnswered());
    }
}
