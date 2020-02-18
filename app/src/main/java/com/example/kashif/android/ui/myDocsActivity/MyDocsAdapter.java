package com.example.kashif.android.ui.myDocsActivity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.bumptech.glide.Glide;
import com.example.kashif.android.R;
import com.example.kashif.android.data.DataManager;
import com.example.kashif.android.data.MyDocsDataModel;
import com.example.kashif.android.databinding.ItemViewMyDocsFilesContentBinding;
import com.example.kashif.android.databinding.ItemViewMyDocsFolderContentBinding;
import com.example.kashif.android.di.ActivityContext;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class MyDocsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    enum myDocsTimeLine {Folder, File}

    @Inject
    DataManager dataManager;

    private Context context;
    private List<MyDocsDataModel> pdfFilesList;
    private List<MyDocsDataModel> pdfFilesListFiltered;
    private MyDocsClickListener myDocsClickListener;

    public interface MyDocsClickListener {
        void onFolderNameClicked(MyDocsDataModel files);
        void onDeleteFolderClicked(MyDocsDataModel files);
        void onDeleteFileClicked(MyDocsDataModel files);
    }

    @Inject
    public MyDocsAdapter(@ActivityContext Context context) {
        this.context = context;
    }

    public void setList(List<MyDocsDataModel> pdfFiles) {
        this.pdfFilesList = pdfFiles;
        this.pdfFilesListFiltered = pdfFiles;
        notifyDataSetChanged();
    }

    public void clearLists() {
        if (pdfFilesList!=null)
            this.pdfFilesList.clear();
        if (pdfFilesListFiltered!=null)
            this.pdfFilesListFiltered.clear();
        notifyDataSetChanged();
    }

    public void setClickListener(MyDocsClickListener myDocsClickListener) {
        this.myDocsClickListener = myDocsClickListener;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == myDocsTimeLine.Folder.ordinal()) {
            ItemViewMyDocsFolderContentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_view_my_docs_folder_content, parent, false);
            return new FolderViewHolder(binding);
        } else {
            ItemViewMyDocsFilesContentBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.item_view_my_docs_files_content, parent, false);
            return new FilesViewHolder(binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                bindFolderView((FolderViewHolder) viewHolder, position);
                break;
            case 1:
                bindFilesView((FilesViewHolder) viewHolder, position);
                break;
        }
    }

    private void bindFolderView(@NonNull FolderViewHolder viewHolder, int position) {
        viewHolder.bind(this.pdfFilesListFiltered.get(position));
    }

    private void bindFilesView(@NonNull FilesViewHolder viewHolder, int position) {
        viewHolder.bind(this.pdfFilesListFiltered.get(position));
    }

    @Override
    public int getItemCount() {
        return pdfFilesListFiltered != null ? pdfFilesListFiltered.size() : 0;
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        switch (holder.getItemViewType()) {
            case 0:
                FolderViewHolder folderViewHolder = ((FolderViewHolder) holder);
                folderViewHolder.bindingFolder.fileNameFolder.setText(null);
                folderViewHolder.bindingFolder.myDocsCbFolder.setOnCheckedChangeListener(null);
                folderViewHolder.bindingFolder.myDocsCbFolder.setChecked(false);
                folderViewHolder.bindingFolder.fileDateSizeFolder.setText(null);
                break;
            case 1:
                FilesViewHolder filesViewHolder = ((FilesViewHolder) holder);
                filesViewHolder.bindingFiles.fileNameFiles.setText(null);
                filesViewHolder.bindingFiles.myDocsCbFiles.setOnCheckedChangeListener(null);
                filesViewHolder.bindingFiles.myDocsCbFiles.setChecked(false);
                filesViewHolder.bindingFiles.fileDateSizeFiles.setText(null);
                break;
        }
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemViewType(int position) {
        if (!pdfFilesListFiltered.get(position).isFile())
            return myDocsTimeLine.Folder.ordinal();
        else
            return myDocsTimeLine.File.ordinal();
    }

    public List<MyDocsDataModel> getSelectedFiles() {
        List<MyDocsDataModel> selectedItems = new ArrayList<>();
        for (MyDocsDataModel file : pdfFilesListFiltered) {
            if (file.isChecked()) {
                selectedItems.add(file);
            }
        }
        return selectedItems;
    }

    public void selectAllFiles(boolean isSelectedAll) {
        try {
            if (pdfFilesListFiltered != null) {
                for (MyDocsDataModel files : pdfFilesListFiltered) {
                    files.setChecked(isSelectedAll);
                }
            }
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MyDocsDataModel getItemAtPosition(int pos) {
        return pdfFilesListFiltered.get(pos);
    }

    public void removeAt(int position) {
        pdfFilesListFiltered.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, pdfFilesListFiltered.size());
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    pdfFilesListFiltered = pdfFilesList;
                } else {
                    List<MyDocsDataModel> filteredList = new ArrayList<>();
                    for (MyDocsDataModel row : pdfFilesList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    pdfFilesListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = pdfFilesListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                pdfFilesListFiltered = (ArrayList<MyDocsDataModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class FolderViewHolder extends RecyclerView.ViewHolder {

        private ItemViewMyDocsFolderContentBinding bindingFolder;

        FolderViewHolder(ItemViewMyDocsFolderContentBinding binding) {
            super(binding.getRoot());
            this.bindingFolder = binding;
        }

        void bind(MyDocsDataModel obj) {
            bindingFolder.setListener(this);
            bindingFolder.setModel(obj);

            if (obj.isChecked())
                bindingFolder.myDocsCbFolder.setChecked(true);
            else
                bindingFolder.myDocsCbFolder.setChecked(false);

            bindingFolder.fileNameFolder.setText(obj.getName());
            bindingFolder.fileDateSizeFolder.setText(String.format("%s Files", obj.getNumberOfFiles()));

            bindingFolder.myDocsCbFolder.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    obj.setChecked(true);
//                    myDocsClickListener.onSelectedFiles(true);
                } else {
                    obj.setChecked(false);
//                    myDocsClickListener.onSelectedFiles(false);
//                    for (MyDocsDataModel file : pdfFilesListFiltered) {
//                        if (file.isChecked()) {
//                            myDocsClickListener.onSelectedFiles(true);
//                            break;
//                        } else {
//                            myDocsClickListener.onSelectedFiles(false);
//                        }
//                    }

                }
            });

            bindingFolder.executePendingBindings();
        }

        public void onFolderNameClicked(MyDocsDataModel file) {
            myDocsClickListener.onFolderNameClicked(file);
        }

        public void onFolderMoreClicked(MyDocsDataModel file) {
            //Creating the instance of PopupMenu
            PopupMenu popup = new PopupMenu(context, bindingFolder.menuIvFolder);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.popup_menu_mydocs_folder, popup.getMenu());
            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.delete_docs_folder_popup:
                        myDocsClickListener.onDeleteFolderClicked(file);
                        return true;
                    default:
                        return true;
                }
            });

            popup.show();//showing popup menu

        }
    }

    public class FilesViewHolder extends RecyclerView.ViewHolder {

        private ItemViewMyDocsFilesContentBinding bindingFiles;

        FilesViewHolder(ItemViewMyDocsFilesContentBinding binding) {
            super(binding.getRoot());
            this.bindingFiles = binding;
        }

        void bind(MyDocsDataModel obj) {
            bindingFiles.setListener(this);
            bindingFiles.setModel(obj);

            if (obj.isChecked())
                bindingFiles.myDocsCbFiles.setChecked(true);
            else
                bindingFiles.myDocsCbFiles.setChecked(false);

            bindingFiles.fileNameFiles.setText(obj.getName());
            bindingFiles.fileDateSizeFiles.setText(String.format("%s - %s", obj.getDate(), obj.getSize()));

            Glide.with(context).load(obj.getPath()).centerCrop().into(bindingFiles.myDocsIvFiles);

            bindingFiles.myDocsCbFiles.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    obj.setChecked(true);
                } else {
                    obj.setChecked(false);
                }
            });

            bindingFiles.executePendingBindings();
        }


        public boolean onLongClick(View v, MyDocsDataModel myDocsDataModel) {
            ClipData.Item item = new ClipData.Item(myDocsDataModel.getPath());
            String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
            ClipData data = new ClipData(myDocsDataModel.getPath(), mimeTypes, item);
            View.DragShadowBuilder dragshadow = new View.DragShadowBuilder(v);
            v.startDrag(data        // data to be dragged
                    , dragshadow   // drag shadow builder
                    , v           // local data about the drag and drop operation
                    , 0          // flags (not currently used, set to 0)
            );
            return true;
        }

        public void onFilesMoreClicked(MyDocsDataModel file) {
            //Creating the instance of PopupMenu
            PopupMenu popup = new PopupMenu(context, bindingFiles.menuIvFiles);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.popup_menu_mydocs_files, popup.getMenu());
            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.delete_docs_files_popup:
                        myDocsClickListener.onDeleteFileClicked(file);
                        return true;

                    default:
                        return true;
                }
            });

            popup.show();//showing popup menu

        }
    }
}
