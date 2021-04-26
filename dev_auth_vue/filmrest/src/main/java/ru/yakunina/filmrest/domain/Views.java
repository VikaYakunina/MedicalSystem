package ru.yakunina.filmrest.domain;

public final class Views {      // final чтоб никто не унаследовал

    public interface IdUser {}                             // только id

    public interface IdNameUser extends IdUser {}             // id и name

    public interface FullUser extends IdNameUser {}       // все атрибуты вместе с паролем
}
