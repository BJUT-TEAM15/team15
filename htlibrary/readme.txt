海天图书馆管理系统

系统现在自带数据库支持, 无须用户再安装数据库管理系统.

系统必需条件:
jdk 5.0 标准版或以上


[1.1版更新] 2007-1-16
使用文件锁, 修改只允许同时运行一个管理系统.

修改系统使用HSQLDB数据库, 这是一个开源的小型数据库. 这个DBMS虽然很迷你, 
但功能却一点儿也不弱. 它支持标准SQL语言的大部分特性, 支持事务操作等等.
而且由于完全使用Java编写, 使得Java应用程序与它可以完全无缝地集成.
缺点是速度会稍微慢一点, 不过对于这个图书馆管理系统来说是绰绰有余的了!

如果你想使用其它更加高级的数据库管理系统, 如SQL Server, MySQL, Oracle...
理论上也不是不可以, 但是会比较麻烦, 因为本管理系统在数据库移植方面做得不太好!

由于我是在当初学Java不久后才编写的这个管理系统, 所以设计, 编码以及测试等很多地方都
做得比较差. 我相信高手一眼就可以看出来, 这是个有着漂亮外表, 而缺陷无数的系统.

从最初实现这个系统到现在, 我的主要精力都放在学习Java EE上. 所以一直未对本图书馆
管理系统进行更新, 在此深表抱歉! 在本学期考试完了以后, 花了一天的时间, 把整个系统
更改为使用HSQLDB, 免去用户安装数据库管理系统的麻烦.

其实我一直想找个时间, 对整个系统进行[重构]. 以优化代码, 改进软件结构等等.
但由于没有太多的空闲时间, 另外我在学习Java EE的过程中遇到很多困难. 所以不能
进行这项工作了, 我想还是等我学好了Java EE和Java Web编程, 到时重新开发一个
B/S架构的网上图书馆或者网上书店好了. 请有兴趣的兄弟与我联系, 一起学习提高!

没有重构的另外一个原因是, 本身系统写得比较复杂. 代码量较多, 软件架构混乱, 界面与
数据库没有分层, 导致修改系统比较困难. 加上最初没有编写单元测试代码, 这也一定程度上
导致没有办法进行重构这项工作. 这也间接地让我认识到了TDD的优点和学习它的必要性 : )


等有了时间和重复发明轮子的心情时, 我会考虑重新实现一下本系统! 


****************************************************
* 如果连接数据库出错(这是我的错, 程序本身设计得不灵活),
* 可以直接输入用户名:demo (无须密码)进入演示版本.     
* 这样可以看到程序的效果, 但无法使用与数据库相关的功能.
* 给您造成的麻烦, 我深表抱歉!!!               
****************************************************


如何启动和运行:

请双击 startup.bat 启动本图书馆管理系统.





谢谢您的使用和支持. 

联系我:  zqw
Email:   zhangqiwen1234@163.com



关于源代码, 请查看以下授权:

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or any later version.
This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

再次谢谢!