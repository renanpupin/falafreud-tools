
Pod::Spec.new do |s|
  s.name         = "falafreud-tools"
  s.version      = "1.0.0"
  s.summary      = "falafreud-tools"
  s.description  = <<-DESC
                  falafreud-tools
                   DESC
  s.homepage     = ""
  s.license      = "MIT"
  # s.license      = { :type => "MIT", :file => "FILE_LICENSE" }
  s.author             = { "author" => "author@domain.cn" }
  s.platform     = :ios, "7.0"
  s.source       = { :git => "https://github.com/renanpupin/falafreud-tools", :tag => "master" }
  s.source_files  = "falafreud-tools/**/*.{h,m}"
  s.requires_arc = true


  s.dependency "React"
  #s.dependency "others"

end
