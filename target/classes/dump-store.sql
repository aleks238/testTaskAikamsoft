PGDMP  5    !            	    {         
   dump_store    16.0    16.0     �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                      false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                      false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                      false            �           1262    16398 
   dump_store    DATABASE     ~   CREATE DATABASE dump_store WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'Russian_Russia.1251';
    DROP DATABASE dump_store;
                postgres    false            �            1259    16407 	   customers    TABLE     �   CREATE TABLE public.customers (
    id bigint NOT NULL,
    name character varying(30) NOT NULL,
    last_name character varying(80) NOT NULL
);
    DROP TABLE public.customers;
       public         heap    postgres    false            �            1259    16406    customers_id_seq    SEQUENCE     y   CREATE SEQUENCE public.customers_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.customers_id_seq;
       public          postgres    false    218                        0    0    customers_id_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE public.customers_id_seq OWNED BY public.customers.id;
          public          postgres    false    217            �            1259    16400    products    TABLE     n   CREATE TABLE public.products (
    id bigint NOT NULL,
    title character varying(255),
    price integer
);
    DROP TABLE public.products;
       public         heap    postgres    false            �            1259    16399    products_id_seq    SEQUENCE     x   CREATE SEQUENCE public.products_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.products_id_seq;
       public          postgres    false    216                       0    0    products_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.products_id_seq OWNED BY public.products.id;
          public          postgres    false    215            �            1259    16441 	   purchases    TABLE     �   CREATE TABLE public.purchases (
    id bigint NOT NULL,
    customer_id bigint NOT NULL,
    product_id bigint NOT NULL,
    purchase_date date
);
    DROP TABLE public.purchases;
       public         heap    postgres    false            �            1259    16440    purchases_id_seq    SEQUENCE     y   CREATE SEQUENCE public.purchases_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.purchases_id_seq;
       public          postgres    false    220                       0    0    purchases_id_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE public.purchases_id_seq OWNED BY public.purchases.id;
          public          postgres    false    219            [           2604    16410    customers id    DEFAULT     l   ALTER TABLE ONLY public.customers ALTER COLUMN id SET DEFAULT nextval('public.customers_id_seq'::regclass);
 ;   ALTER TABLE public.customers ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    217    218    218            Z           2604    16403    products id    DEFAULT     j   ALTER TABLE ONLY public.products ALTER COLUMN id SET DEFAULT nextval('public.products_id_seq'::regclass);
 :   ALTER TABLE public.products ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    216    215    216            \           2604    16444    purchases id    DEFAULT     l   ALTER TABLE ONLY public.purchases ALTER COLUMN id SET DEFAULT nextval('public.purchases_id_seq'::regclass);
 ;   ALTER TABLE public.purchases ALTER COLUMN id DROP DEFAULT;
       public          postgres    false    219    220    220            �          0    16407 	   customers 
   TABLE DATA           8   COPY public.customers (id, name, last_name) FROM stdin;
    public          postgres    false    218   W       �          0    16400    products 
   TABLE DATA           4   COPY public.products (id, title, price) FROM stdin;
    public          postgres    false    216   �       �          0    16441 	   purchases 
   TABLE DATA           O   COPY public.purchases (id, customer_id, product_id, purchase_date) FROM stdin;
    public          postgres    false    220   �                  0    0    customers_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.customers_id_seq', 4, true);
          public          postgres    false    217                       0    0    products_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.products_id_seq', 3, true);
          public          postgres    false    215                       0    0    purchases_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.purchases_id_seq', 7, true);
          public          postgres    false    219            `           2606    16412    customers customers_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.customers
    ADD CONSTRAINT customers_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.customers DROP CONSTRAINT customers_pkey;
       public            postgres    false    218            ^           2606    16405    products products_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.products
    ADD CONSTRAINT products_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.products DROP CONSTRAINT products_pkey;
       public            postgres    false    216            b           2606    16446    purchases purchases_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.purchases
    ADD CONSTRAINT purchases_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.purchases DROP CONSTRAINT purchases_pkey;
       public            postgres    false    220            c           2606    16447 $   purchases purchases_customer_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.purchases
    ADD CONSTRAINT purchases_customer_id_fkey FOREIGN KEY (customer_id) REFERENCES public.customers(id);
 N   ALTER TABLE ONLY public.purchases DROP CONSTRAINT purchases_customer_id_fkey;
       public          postgres    false    220    4704    218            d           2606    16452 #   purchases purchases_product_id_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.purchases
    ADD CONSTRAINT purchases_product_id_fkey FOREIGN KEY (product_id) REFERENCES public.products(id);
 M   ALTER TABLE ONLY public.purchases DROP CONSTRAINT purchases_product_id_fkey;
       public          postgres    false    216    4702    220            �   6   x�3��JL���y\F�N�99��9 �1�S~�oiQAF%�	D
�2F��� G�      �   .   x�3�L*JML�41�2�L,(�I�444�2������47����� �g�      �   ;   x�3�4B##c]K]#.#TS.c!`�e�i���U�	����&�f��qqq �;^     