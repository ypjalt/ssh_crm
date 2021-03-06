package action;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import domain.Customer;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import service.CustomerService;
import utils.PageBean;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;


@Scope("prototype")
@Controller("customerAction")
public class CustomerAction extends ActionSupport implements ModelDriven<Customer> {
    private Customer customer = new Customer();
    @Resource(name = "customerService")
    private CustomerService cs;

    //上传的文件会自动封装到File对象
    private File photo;
    //在提交键名后加上固定后缀FileName,文件名称会自动封装到属性中
    private String photoFileName;
    //在提交键名后加上固定后缀ContentType,文件MIME类型名称会自动封装到属性中
    private String photoContentType;

    // 当前页数
    private Integer currentPage;
    // 每条显示页数
    private Integer pageSize;

    //查询统计分析类型
    private String type;

    public String list() throws Exception {
        DetachedCriteria dc = DetachedCriteria.forClass(Customer.class);
        if (StringUtils.isNotBlank(customer.getCust_name())) {
            dc.add(Restrictions.like("cust_name", "%" + customer.getCust_name() + "%"));
        }
        PageBean pb = cs.getPageBean(dc, currentPage, pageSize);
        ActionContext.getContext().put("pageBean", pb);
        return "list";
    }

    public String selectlist() throws Exception {
        DetachedCriteria dc = DetachedCriteria.forClass(Customer.class);
        if (StringUtils.isNotBlank(customer.getCust_name())) {
            dc.add(Restrictions.like("cust_name", "%" + customer.getCust_name() + "%"));
        }
        PageBean pb = cs.getPageBean(dc, currentPage, pageSize);
        ActionContext.getContext().put("pageBean", pb);
        return "selectlist";
    }

    public String add() throws Exception {
        //---------------------------------
        //文件保存路径
        if (photo != null) {
            photo.renameTo(new File("E:/hh.jpg"));
            System.out.println(photoFileName);
            System.out.println(photoContentType);
        }
        //---------------------------------
        //1.调用service
        cs.save(customer);
        return "toList";
    }

    public String count() throws Exception {
        List<Object[]> listCount = cs.getCount(type);
        ActionContext.getContext().put("list", listCount);
        return "count";
    }

    public String toEdit() throws Exception {
        //1调用Service根据id获得客户对象
        Customer c = cs.getById(customer.getCust_id());
        //2将客户对象放置到request域,并转发到编辑界面
        ActionContext.getContext().put("customer", c);

        return "edit";
    }

    public void setCs(CustomerService cs) {
        this.cs = cs;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public Customer getModel() {
        return customer;
    }

    public File getPhoto() {
        return photo;
    }

    public void setPhoto(File photo) {
        this.photo = photo;
    }

    public String getPhotoFileName() {
        return photoFileName;
    }

    public void setPhotoFileName(String photoFileName) {
        this.photoFileName = photoFileName;
    }

    public String getPhotoContentType() {
        return photoContentType;
    }

    public void setPhotoContentType(String photoContentType) {
        this.photoContentType = photoContentType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
