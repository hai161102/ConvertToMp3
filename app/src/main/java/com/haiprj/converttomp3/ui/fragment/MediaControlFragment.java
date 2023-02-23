package com.haiprj.converttomp3.ui.fragment;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.haiprj.converttomp3.AppCallback;
import com.haiprj.converttomp3.Const;
import com.haiprj.converttomp3.R;
import com.haiprj.converttomp3.databinding.FragmentMusicEditBarBinding;
import com.haiprj.converttomp3.models.FileModel;
import com.haiprj.converttomp3.ui.activity.PlayMusicActivity;
import com.haiprj.converttomp3.utils.AppUtils;
import com.haiprj.converttomp3.utils.ReplayState;
import com.haiprj.converttomp3.utils.SharePreferenceUtils;
import com.haiprj.converttomp3.widget.TimeLineView;

import java.io.IOException;

public class MediaControlFragment extends BaseFragment<FragmentMusicEditBarBinding> {

    public static final String NEXT_AND_NONE = "NEXT_AND_NONE";
    public static final String NEXT_AND_REPLAY_ALL = "NEXT_AND_REPLAY_ALL";
    public static final String NEXT_AND_REPLAY_ONE = "NEXT_AND_REPLAY_ONE";
    private MediaPlayer mediaPlayer;
    private FileModel fileModel;

    private AppCallback callback;

    private ReplayState replayState;


    public ReplayState getReplayState() {
        return replayState;
    }

    public MediaControlFragment(FileModel FileModel) {
        this.fileModel = FileModel;
    }

    @Override
    protected void initView() {
        int repeatState = SharePreferenceUtils.getInstance().getInt(Const.REPLAY_STATE_CURRENT, ReplayState.NONE.ordinal());
        if (repeatState == ReplayState.NONE.ordinal()) {
            replayState = ReplayState.NONE;
        }
        else if (repeatState == ReplayState.REPLAY_ALL.ordinal()) {
            replayState = ReplayState.REPLAY_ALL;
        }
        else if (repeatState == ReplayState.REPLAY_ONE.ordinal()) {
            replayState = ReplayState.REPLAY_ONE;
        }
        setupViewReplay();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnPreparedListener(mp -> {
            binding.mediaController.timeSeekBar.setMax(1000);
            binding.timeLine.init(new TimeLineView.TimeLineListener() {
                @Override
                public int getCurrentPosition() {
                    binding.mediaController.currentTime.setText(AppUtils.stringForTime(mediaPlayer.getCurrentPosition()));
                    binding.mediaController.timeSeekBar.setProgress((int) (1000L * mp.getCurrentPosition() / mp.getDuration()), true);
                    return mediaPlayer.getCurrentPosition();
                }

                @Override
                public int getDuration() {
                    return mediaPlayer.getDuration();
                }
            });
            binding.mediaController.duration.setText(AppUtils.stringForTime(mediaPlayer.getDuration()));
            binding.timeLine.setPrepared();
            mediaPlayer.start();
            binding.mediaController.play.setImageResource(R.drawable.ic_pause);
        });
        mediaPlayer.setOnCompletionListener(mp -> {
            binding.timeLine.setComplete();
            binding.mediaController.play.setImageResource(R.drawable.ic_play);
            //onAutoNext();
//            switch (replayState) {
//                case NONE:
//                case REPLAY_ALL:
//                    onNext();
//                    break;
//                case REPLAY_ONE:
//                    mediaPlayer.start();
//                    break;
//            }
        });
        reload();
        setupViewRandom();



    }

    private void onAutoNext() {
        if (replayState == ReplayState.NONE) {
            callback.action(NEXT_AND_NONE, fileModel);
        }
        else if (replayState == ReplayState.REPLAY_ALL) {
            callback.action(NEXT_AND_REPLAY_ALL, fileModel);
        }
        else if (replayState == ReplayState.REPLAY_ONE) {
            callback.action(NEXT_AND_REPLAY_ONE, fileModel);
        }
    }


    private void setupViewReplay() {
        switch (replayState) {
            case NONE:
                binding.mediaController.repeatMusic.setImageResource(R.drawable.baseline_repeat_24);
                binding.mediaController.repeatMusic.setImageTintList(ColorStateList.valueOf(requireContext().getColor(R.color.white)));
                break;
            case REPLAY_ALL:
                binding.mediaController.repeatMusic.setImageResource(R.drawable.baseline_repeat_24);
                binding.mediaController.repeatMusic.setImageTintList(ColorStateList.valueOf(requireContext().getColor(R.color.app_color)));
                break;
            case REPLAY_ONE:
                binding.mediaController.repeatMusic.setImageResource(R.drawable.baseline_repeat_one_24);
                binding.mediaController.repeatMusic.setImageTintList(ColorStateList.valueOf(requireContext().getColor(R.color.app_color)));
                break;
        }
    }

    private void setupViewRandom() {
        binding.mediaController.randomMusic.setImageTintList(
                !((PlayMusicActivity) requireActivity()).isRandom()
                        ? ColorStateList.valueOf(requireContext().getColor(R.color.white))
                        : ColorStateList.valueOf(requireContext().getColor(R.color.app_color))
        );
    }

    public void setCallback(AppCallback callback) {
        this.callback = callback;
    }

    private void reload() {
        Glide.with(this).load(fileModel.getFileUri()).into(binding.musicImage);
        binding.fileName.setText(fileModel.getDisplayName());
        try {
            mediaPlayer.setDataSource(requireContext(), fileModel.getFileUri());
            mediaPlayer.prepareAsync();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void addEvent() {
        binding.mediaController.randomMusic.setOnClickListener(v -> {
            ((ImageView) v).setImageTintList(ColorStateList.valueOf(requireContext().getColor(R.color.app_color)));
        });

        binding.mediaController.randomMusic.setOnClickListener(v -> {
            callback.action("random");

            setupViewRandom();
        });

        binding.mediaController.repeatMusic.setOnClickListener(v -> {
            switch (replayState) {
                case NONE:
                    replayState = ReplayState.REPLAY_ALL;
                    break;
                case REPLAY_ALL:
                    replayState = ReplayState.REPLAY_ONE;
                    break;
                case REPLAY_ONE:
                    replayState = ReplayState.NONE;
                    break;
            }
            SharePreferenceUtils.getInstance().setInt(Const.REPLAY_STATE_CURRENT, replayState.ordinal());
            setupViewReplay();
        });
        binding.mediaController.play.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                binding.mediaController.play.setImageResource(R.drawable.ic_play);
                mediaPlayer.pause();
            } else {
                binding.mediaController.play.setImageResource(R.drawable.ic_pause);
                mediaPlayer.start();
            }
        });
        binding.back.setOnClickListener(v -> {
            callback.action("finish");
        });

        binding.share.setOnClickListener(v -> {
            AppUtils.shareFile(requireContext(), fileModel.getFileUri());
        });

        binding.mediaController.next.setOnClickListener(v -> {
            onNext();
        });

        binding.mediaController.previous.setOnClickListener(v -> {
            onPrevious();
        });

        binding.mediaController.rewind.setOnClickListener(v -> {
            onRewind();
        });

        binding.mediaController.forward.setOnClickListener(v -> {
            onForward();
        });

        binding.mediaController.timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo((int) (seekBar.getProgress() * mediaPlayer.getDuration() / 1000L));
            }
        });
    }

    private void onForward() {
        int current = binding.mediaController.timeSeekBar.getProgress();
        int max = binding.mediaController.timeSeekBar.getMax();
        current += (max / 10);
        this.binding.mediaController.timeSeekBar.setProgress(current, true);
        float msec = this.binding.mediaController.timeSeekBar.getProgress() * this.mediaPlayer.getDuration() / 1000f;
        this.mediaPlayer.seekTo((int) msec);
    }

    private void onRewind() {
        int current = binding.mediaController.timeSeekBar.getProgress();
        int max = binding.mediaController.timeSeekBar.getMax();
        current -= (max / 10);

        this.binding.mediaController.timeSeekBar.setProgress(current, true);
        float msec = this.binding.mediaController.timeSeekBar.getProgress() * this.mediaPlayer.getDuration() / 1000f;
        this.mediaPlayer.seekTo((int) msec);
    }

    private void onPrevious() {
        callback.action("previous", fileModel);
    }

    private void onNext() {
        callback.action("next", fileModel);
    }

    public void loadData(FileModel FileModel) {
        this.fileModel = FileModel;
        mediaPlayer.reset();
        reload();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.timeLine.destroy();
        mediaPlayer.reset();
        mediaPlayer.release();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_music_edit_bar;
    }

}
